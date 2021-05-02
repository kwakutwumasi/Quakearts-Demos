package com.quakearts.auth.server.totpqrenroller.client.http;

import java.net.URL;

import javax.inject.Singleton;

import com.quakearts.rest.client.HttpClientBuilder;

@Singleton
public class TOTPHttpClientBuilder 
	extends HttpClientBuilder<TOTPHttpClient> {
	
	public void setHttpClient(TOTPHttpClient totpHttpClient) {
		httpClient = totpHttpClient;
	}
	
	public HttpClientBuilder<TOTPHttpClient> setFileAs(String file) {
		httpClient.file = file;
		return this;
	}
	
	@Override
	public HttpClientBuilder<TOTPHttpClient> setURLAs(URL url) {
		setFileAs(url.getFile());
		return super.setURLAs(url);
	}
	
	@Override
	public TOTPHttpClient thenBuild() {
		return httpClient;
	}
}
