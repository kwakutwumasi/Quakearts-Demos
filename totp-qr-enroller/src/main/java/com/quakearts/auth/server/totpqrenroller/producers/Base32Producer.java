package com.quakearts.auth.server.totpqrenroller.producers;

import javax.enterprise.inject.Produces;

import org.apache.commons.codec.binary.Base32;

public class Base32Producer {
	private Base32 base32 = new Base32();
	
	@Produces
	public Base32 getBase32() {
		return base32;
	}
}
