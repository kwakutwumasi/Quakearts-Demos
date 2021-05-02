package com.quakearts.auth.server.totpqrenroller.beans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.codec.binary.Base32;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.quakearts.appbase.Main;
import com.quakearts.auth.server.totpqrenroller.beans.exception.EnrollmentException;
import com.quakearts.auth.server.totpqrenroller.client.http.TOTPHttpClient;
import com.quakearts.auth.server.totpqrenroller.client.http.model.ActivationRequest;
import com.quakearts.auth.server.totpqrenroller.client.http.model.ProvisioningResponse;
import com.quakearts.auth.server.totpqrenroller.model.EnrollmentRecord;
import com.quakearts.auth.server.totpqrenroller.model.EnrollmentRequest;
import com.quakearts.rest.client.exception.HttpClientException;
import com.quakearts.security.cryptography.CryptoResource;
import com.quakearts.security.cryptography.cdi.CryptoInstance;
import com.quakearts.security.cryptography.cdi.CryptoResourceHandle;
import com.quakearts.security.cryptography.exception.IllegalCryptoActionException;
import com.quakearts.security.cryptography.exception.KeyProviderException;
import com.quakearts.webapp.facelets.base.BaseBean;
import com.quakearts.webapp.orm.DataStore;
import com.quakearts.webapp.orm.DataStoreFactory;
import com.quakearts.webapp.orm.cdi.annotation.DataStoreFactoryHandle;

@Named("qrCodeEnroller")
@ViewScoped
public class QRCodeEnrollerBean extends BaseBean {
	
	private static final String INVALID_DATA = "Invalid Data";

	/**
	 * 
	 */
	private static final long serialVersionUID = -4902396830894946841L;

	private static final String OAUTH_URI_FORMAT =
			"otpauth://totp/%1$s?secret=%2$s&"
			+ "issuer=fastpacetransfer.com&"
			+ "algorithm=SHA1&"
			+ "period=30";
	
	private static final String DATA_URI_FORMAT = "data:image/png;base64,%s";
	
	@Inject @CryptoResourceHandle @CryptoInstance("qrenroller")
	private transient CryptoResource cryptoResource;
	
	@Inject @DataStoreFactoryHandle
	private transient DataStoreFactory dataStoreFactory;
	
	@Inject
	private transient ObjectMapper objectMapper;

	@Inject
	private transient TOTPHttpClient totpHttpClient;
	
	@Inject
	private transient QRCodeWriter qrCodeWriter;
	
	@Inject
	private transient Base32 base32;
	
	private String code;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	private String qrImageDataURI;
	
	public String getQrImageDataURI() {
		return qrImageDataURI;
	}
	
	private String fullName;
	
	public String getFullName() {
		return fullName;
	}
	
	public void generateQRCode(AjaxBehaviorEvent event) {
		qrImageDataURI = null;
		fullName = null;
		try {
			validateCode();		
			EnrollmentRequest request = getEnrollmentRequest();
			EnrollmentRecord enrollmentRecord = loadOrCreateEnrollmentRecord(request);
			BitMatrix bitMatrix = generateBitMatrix(request, enrollmentRecord);
			writeOut(bitMatrix);			
		} catch (EnrollmentException e) {
			// Do nothing
		}
	}

	private void validateCode() throws EnrollmentException {
		if(code == null) {
			addError(INVALID_DATA, "Code is required", FacesContext.getCurrentInstance());
			throw new EnrollmentException();
		}
	}

	private EnrollmentRequest getEnrollmentRequest() throws EnrollmentException {
		String encryptedCode = code.replace("-", "")
				.replace("\r", "")
				.replace("\n", "")
				.toLowerCase();
		
		String decryptedCode;
		try {
			decryptedCode = cryptoResource.doDecrypt(encryptedCode);
		} catch (IllegalCryptoActionException | KeyProviderException e) {
			Main.log.error("Unable to decrypt", e);
			addError(INVALID_DATA, "The Code is not valid", FacesContext.getCurrentInstance());
			throw new EnrollmentException();
		}
		
		EnrollmentRequest request;
		try {
			request = objectMapper.readValue(decryptedCode, EnrollmentRequest.class);
		} catch (JsonProcessingException e) {
			Main.log.error("Unable to marshall", e);
			addError(INVALID_DATA, "The Code is not properly formatted", FacesContext.getCurrentInstance());
			throw new EnrollmentException();
		}
		fullName = request.getFullName();
		return request;
	}

	private EnrollmentRecord loadOrCreateEnrollmentRecord(EnrollmentRequest request) throws EnrollmentException {
		DataStore dataStore = dataStoreFactory.getDataStore();
		EnrollmentRecord enrollmentRecord = dataStore.get(EnrollmentRecord.class, code);
		if(enrollmentRecord == null) {
			try {
				ProvisioningResponse response = totpHttpClient.provision(request.getDeviceId());
				enrollmentRecord = new EnrollmentRecord();
				enrollmentRecord.setCode(code);
				enrollmentRecord.setSecret(CryptoResource.hexAsByte(response.getSeed()));
				enrollmentRecord.setInitialCounter(response.getInitialCounter());
				dataStore.save(enrollmentRecord);
			} catch (IOException | HttpClientException | IllegalCryptoActionException e) {
				Main.log.error("Unable to provision", e);
				addError(INVALID_DATA, "The authentication device could not be provisioned",
						FacesContext.getCurrentInstance());
				throw new EnrollmentException();
			}
		}
		return enrollmentRecord;
	}

	private BitMatrix generateBitMatrix(EnrollmentRequest request, EnrollmentRecord enrollmentRecord)
			throws EnrollmentException {
		String base32Secret = base32.encodeAsString(enrollmentRecord.getSecret());
		String otpAuthURI = String.format(OAUTH_URI_FORMAT, request.getAlias(), base32Secret, enrollmentRecord.getInitialCounter());
		
		BitMatrix bitMatrix;
		try {
			bitMatrix = qrCodeWriter.encode(otpAuthURI, BarcodeFormat.QR_CODE, 600, 600);
		} catch (WriterException e) {
			Main.log.error("Unable to generate", e);
			addError(INVALID_DATA, "The Code could not be written as a QR String", FacesContext.getCurrentInstance());
			throw new EnrollmentException();
		}
		return bitMatrix;
	}

	private void writeOut(BitMatrix bitMatrix) throws EnrollmentException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(bitMatrix.getHeight()*bitMatrix.getWidth()/2);
		try {
			MatrixToImageWriter.writeToStream(bitMatrix, "png", bos);
		} catch (IOException e) {
			Main.log.error("Unable to write", e);
			addError(INVALID_DATA, "The Code could not be written as a QR String", FacesContext.getCurrentInstance());
			throw new EnrollmentException();
		}
		
		qrImageDataURI = String.format(DATA_URI_FORMAT, Base64.getEncoder().encodeToString(bos.toByteArray()));
	}
	
	private String otpCode;
	
	public String getOtpCode() {
		return otpCode;
	}
	
	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}
	
	public void activateDevice(AjaxBehaviorEvent event) {
		try {
			validateCode();
			validateOtpCode();
			DataStore dataStore = dataStoreFactory.getDataStore();
			EnrollmentRecord enrollmentRecord = loadAndValidateEnrollmentRecord(dataStore);		
			doDeviceActivation();
			enrollmentRecord.setActivated(true);
			dataStore.update(enrollmentRecord);
			addMessage("Success", "Your TOTP Device has been activated", FacesContext.getCurrentInstance());
		} catch (EnrollmentException e) {
			// Do nothing
		}
	}

	private void validateOtpCode() throws EnrollmentException {
		if(otpCode == null) {
			addError(INVALID_DATA, "OTP Code is required", FacesContext.getCurrentInstance());
			throw new EnrollmentException();
		}
	}

	private EnrollmentRecord loadAndValidateEnrollmentRecord(DataStore dataStore) throws EnrollmentException {
		EnrollmentRecord enrollmentRecord = dataStore.get(EnrollmentRecord.class, code);
		if(enrollmentRecord == null) {
			addError(INVALID_DATA, "The authentication device has not been provisioned", FacesContext.getCurrentInstance());
			throw new EnrollmentException();
		}
		if(enrollmentRecord.isActivated()) {
			addError(INVALID_DATA, "The authentication device has already been activated", FacesContext.getCurrentInstance());
			throw new EnrollmentException();
		}
		return enrollmentRecord;
	}
	
	private void doDeviceActivation() throws EnrollmentException {
		EnrollmentRequest request = getEnrollmentRequest();
		
		ActivationRequest activationRequest = new ActivationRequest();
		activationRequest.setAlias(request.getAlias());
		activationRequest.setToken(otpCode);
		
		try {
			totpHttpClient.activate(request.getDeviceId(), activationRequest);
		} catch (IOException | HttpClientException e) {
			Main.log.error("Unable to provision", e);
			addError(INVALID_DATA, "The authentication device could not be activated",
					FacesContext.getCurrentInstance());
			throw new EnrollmentException();
		}
	}
}
