package com.quakearts.auth.server.totpqrenroller.model;

public class EnrollmentRequest {
	private String deviceId;
	private String alias;
	private String fullName;

	public EnrollmentRequest() {}
	
	public EnrollmentRequest(String deviceId, String alias, String fullName) {
		this.deviceId = deviceId;
		this.alias = alias;
		this.fullName = fullName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getAlias() {
		return alias;
	}

	public String getFullName() {
		return fullName;
	}

}
