package com.quakearts.auth.server.totpqrenroller.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.quakearts.security.cryptography.jpa.EncryptedBytesConverter;

@Entity
public class EnrollmentRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2997414296664128305L;
	@Id
	@Column(length = 500)
	private String code;
	@Column(nullable = false)
	@Convert(converter = EncryptedBytesConverter.class)
	private byte[] secret;
	@Column(nullable = false)
	private long initialCounter;
	@Column(nullable = false)
	private boolean activated;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public byte[] getSecret() {
		return secret;
	}

	public void setSecret(byte[] secret) {
		this.secret = secret;
	}

	public long getInitialCounter() {
		return initialCounter;
	}

	public void setInitialCounter(long initialCounter) {
		this.initialCounter = initialCounter;
	}

	public boolean isActivated() {
		return activated;
	}
	
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
}
