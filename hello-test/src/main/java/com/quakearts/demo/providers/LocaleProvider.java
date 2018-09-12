package com.quakearts.demo.providers;

import java.util.Locale;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class LocaleProvider {

	@Inject
	HttpServletRequest request;
	
	@Produces
	public Locale getUserLocal() {
		String localeString = request.getHeader("accept-language");
		if(localeString!=null)
			return Locale.forLanguageTag(localeString);
		
		return Locale.US;
	}
}
