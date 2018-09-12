package com.quakearts.demo.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.quakearts.demo.filter.AcceptContainerResponse;
import com.quakearts.demo.providers.ValidationExceptionMapper;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

@ApplicationPath("/")
public class HelloApplication extends Application {
	
	Set<Class<?>> classes = new HashSet<>(Arrays.asList(AcceptContainerResponse.class,
			FileResource.class,
			GreetingResource.class,
			ValidationExceptionMapper.class,
			OpenApiResource.class));
	
	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}
}
