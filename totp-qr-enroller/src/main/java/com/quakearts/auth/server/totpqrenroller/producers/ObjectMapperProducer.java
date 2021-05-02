package com.quakearts.auth.server.totpqrenroller.producers;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
public class ObjectMapperProducer {

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Produces
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
}
