package com.quakearts.demo.rest.beans;

import java.text.MessageFormat;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import com.quakearts.demo.Main;

@Singleton
public class GreetingObserver {
	public void greeted(@Observes GreetingEvent event) {
		Main.LOGGER.info(MessageFormat.format("Received event. Greeted: {0}",event.getGreeted()));
	}
}
