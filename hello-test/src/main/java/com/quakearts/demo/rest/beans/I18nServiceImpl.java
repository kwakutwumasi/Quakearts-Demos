package com.quakearts.demo.rest.beans;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Singleton;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

@Singleton
public class I18nServiceImpl implements I18nService {

	private Map<Locale, String> greetings = Collections.synchronizedMap(new HashMap<>());
	
	@Override
	public String getMessage(Locale locale) {
		if(!greetings.containsKey(locale)) {
			ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
			greetings.put(locale, bundle.getString("greeting"));
		}
		
		return greetings.get(locale);
	}

	@Override
	public void updateMessage(Locale locale, String greeting) {
		if(!greetings.containsKey(locale))
			throw new NotFoundException();
		
		greetings.put(locale, greeting);
	}
	
	@Override
	public void createMessage(Locale locale, String greeting) {
		if(greetings.containsKey(locale))
			throw new BadRequestException("Greeting for this locale has already been created. PUT to update");
		
		greetings.put(locale, greeting);
	}
}
