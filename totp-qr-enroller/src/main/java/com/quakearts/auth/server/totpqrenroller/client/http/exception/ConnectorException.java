package com.quakearts.auth.server.totpqrenroller.client.http.exception;

import com.quakearts.auth.server.totpqrenroller.client.http.model.ErrorResponse;
import com.quakearts.rest.client.exception.HttpClientException;

public class ConnectorException extends HttpClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1168001922075034516L;

	private final ErrorResponse response;
	private final int httpCode;
	
	public ConnectorException(ErrorResponse response, int httpCode) {
		super("Connector Error");
		this.response = response;
		this.httpCode = httpCode;
	}
	
	public ErrorResponse getResponse() {
		return response;
	}
	
	public int getHttpCode() {
		return httpCode;
	}
}
