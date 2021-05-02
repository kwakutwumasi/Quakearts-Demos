package com.quakearts.auth.server.totpqrenroller.beans;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("helpBean")
@ViewScoped
public class HelpBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -206597250561347830L;
	
	public enum Mode {
		MICROSOFT,
		GOOGLE;
	}
	
	private Mode mode = Mode.MICROSOFT;
	
	public Mode getMode() {
		return mode;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public String getPhone() {
		return System.getProperty("contact.phone", "233201234567");
	}
	
	public String getEmail() {
		return System.getProperty("contact.email", "email@server.com");
	}
}
