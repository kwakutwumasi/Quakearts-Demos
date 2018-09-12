package com.quakearts.demo.rest.beans;

public class ErrorResponse {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public ErrorResponse withMessageAs(String message) {
		setMessage(message);
		return this;
	}
	
}
