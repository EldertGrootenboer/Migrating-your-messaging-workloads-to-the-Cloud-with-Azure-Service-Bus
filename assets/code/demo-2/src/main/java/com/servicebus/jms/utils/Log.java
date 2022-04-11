package com.servicebus.jms.utils;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	public static void receivedMessage(String source, Message message) throws JMSException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String timestamp = formatter.format(new Date());

		if (message instanceof BytesMessage) {
			System.out.println(timestamp + " Bytes message received from: " + source + ". Message size: "
					+ ((BytesMessage) message).getBodyLength() + " bytes");
		} else {
			System.out.println(timestamp + " Message received from: " + source + ". Message: "
					+ ((TextMessage) message).getText());
		}
	}

	public static void sentMessage(String destination, Message message) throws JMSException {
		sentMessage(destination, ((TextMessage) message).getText());
	}

	public static void sentMessage(String destination, BytesMessage message) throws JMSException {
		sentMessage(destination, message.readUTF());
	}

	public static void sentMessage(String destination, String message) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String timestamp = formatter.format(new Date());
		System.out.println(timestamp + " Message sent to: " + destination + ". Message: " + message);
	}

	public static void section(String section) {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("-------------- " + section + " --------------");
	}

	public static void step(String step) {
		System.out.println("-------------- " + step + " --------------");
	}
}