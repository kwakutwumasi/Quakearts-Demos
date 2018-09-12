package com.quakearts.demo.rest.beans;

public class GreetingEvent {
	private String greeted;

	public String getGreeted() {
		return greeted;
	}

	public void setGreeted(String greeted) {
		this.greeted = greeted;
	}
	
	public GreetingEvent withGreetedAs(String greeted) {
		setGreeted(greeted);
		return this;
	}
}
