package com.quakearts.auth.server.totpqrenroller.producers;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.google.zxing.qrcode.QRCodeWriter;

@Singleton
public class QRCodeWriterProducer {
	private QRCodeWriter qrCodeWriter = new QRCodeWriter();

	@Produces
	public QRCodeWriter getQrCodeWriter() {
		return qrCodeWriter;
	}
}
