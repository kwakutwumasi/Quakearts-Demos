package com.quakearts.auth.server.totpqrenroller.command;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.UUID;

import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quakearts.auth.server.totpqrenroller.model.EnrollmentRequest;
import com.quakearts.security.cryptography.CryptoResource;
import com.quakearts.security.cryptography.exception.IllegalCryptoActionException;
import com.quakearts.security.cryptography.exception.KeyProviderException;
import com.quakearts.security.cryptography.factory.CryptoServiceProducer;
import com.quakearts.utilities.annotation.CommandMetadata;
import com.quakearts.utilities.annotation.CommandParameterMetadata;
import com.quakearts.utilities.exception.CommandParameterException;
import com.quakearts.utilities.impl.CommandBase;

@CommandMetadata(value="generate-code", description = "Generate User Codes for Microsoft Authenticator Enrollment",
		parameters = {
				@CommandParameterMetadata(value="username", alias="u", required = true),
				@CommandParameterMetadata(value="fullname", alias="f", required = true)
		})
public class GenerateCodeCommand extends CommandBase {

	private static final Logger log = LoggerFactory.getLogger(GenerateCodeCommand.class);
	
	@Override
	public void execute() throws CommandParameterException {
		ObjectMapper objectMapper = new ObjectMapper();
		EnrollmentRequest enrollmentRequest = new EnrollmentRequest(UUID.randomUUID().toString()
				.replace("-", "").toUpperCase(), getCommandParametersMap()
				.get("username").getValue(), 
				getCommandParametersMap().get("fullname").getValue());
		
		String json; 
		try {
			json = objectMapper.writeValueAsString(enrollmentRequest);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Unable to encrypt data", e);
		}
				
		CryptoResource cryptoResource; 
		try {
			cryptoResource = CryptoServiceProducer.getCryptoService()
					.getCryptoResource("qrenroller");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchAlgorithmException
				| NoSuchPaddingException | NoSuchProviderException | IOException e) {
			throw new IllegalStateException("Unable to load encryption", e);
		}
		
		String encryptedJson;
		try {
			encryptedJson = cryptoResource.doEncrypt(json).toUpperCase();
		} catch (IllegalCryptoActionException | KeyProviderException e) {
			throw new IllegalStateException("Unable to encrypt data", e);
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		for(int ch=0; ch<encryptedJson.length(); ch++) {
			stringBuilder.append(encryptedJson.charAt(ch));
			if(ch>0 && (ch+1) % 8 == 0) {
				if((ch+1) % 32 == 0)
					stringBuilder.append("\r\n");
				else
					stringBuilder.append("-");
			}
		}
		
		log.info("Enrollment Code: \n{}", stringBuilder);
	}
}
