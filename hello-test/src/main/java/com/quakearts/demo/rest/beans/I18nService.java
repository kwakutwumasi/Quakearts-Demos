package com.quakearts.demo.rest.beans;

import java.util.Locale;

public interface I18nService {
	String getMessage(Locale locale);
	void updateMessage(Locale locale, String greeting);
	void createMessage(Locale locale, String greeting);
}
