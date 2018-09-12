package com.quakearts.demo.rest.beans;

import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

@Decorator @Priority(Interceptor.Priority.APPLICATION)
public abstract class MasterGreetingDecorator implements GreetingService {
	
	@Inject @Delegate @Any
	private GreetingService greetingService;
	
	@Override
	public String generateHello(String name, Locale locale) {
		String generated = greetingService.generateHello(name, locale);
		if(generated.contains("Kwaku"))
			return MessageFormat.format("{0}. I bow to you, master", generated);
			
		return generated;
	}
}
