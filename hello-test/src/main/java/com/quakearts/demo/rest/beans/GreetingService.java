package com.quakearts.demo.rest.beans;

import java.util.Locale;

public interface GreetingService {

	String generateHello(String name, Locale locale);
	void updateHello(Locale locale, String greeting);
	void createHello(Locale locale, String greeting);
}