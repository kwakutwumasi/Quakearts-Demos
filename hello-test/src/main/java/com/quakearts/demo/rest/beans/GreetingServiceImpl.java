package com.quakearts.demo.rest.beans;

import java.text.MessageFormat;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@RequestScoped
public class GreetingServiceImpl implements GreetingService {

	@Inject
	private I18nService i18nService;
	@Inject
	private Event<GreetingEvent> greetingBroadCaster;
	
	@Override
	public String generateHello(String name, Locale locale) {
		greetingBroadCaster.fire(new GreetingEvent().withGreetedAs(name));
		return MessageFormat.format(i18nService.getMessage(locale),name);
	}

	@Override
	public void updateHello(Locale locale, String greeting) {
		i18nService.updateMessage(locale, greeting);
	}
	
	@Override
	public void createHello(Locale locale, String greeting) {
		i18nService.createMessage(locale, greeting);
	}	
}
