package com.servicebus.jms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Message;

public class Log {

	public static void ReceivedMessage(String source, Message message) throws JMSException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String timestamp = formatter.format(new Date());
		System.out.println(timestamp + " Message received from: " + source + ". Message: " + ((TextMessage) message).getText());
	}

	public static void SentMessage(String destination, Message message) throws JMSException {
		SentMessage(destination, ((TextMessage) message).getText());
	}

	public static void SentMessage(String destination, String message) throws JMSException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String timestamp = formatter.format(new Date());
		System.out.println(timestamp + " Message sent to: " + destination + ". Message: " + message);
	}

	public static void Section(String section) {
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("--------------" + section + "--------------");
	}
}