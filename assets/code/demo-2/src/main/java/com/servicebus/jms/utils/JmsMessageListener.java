package com.servicebus.jms.utils;

import javax.jms.JMSException;
import javax.jms.Message;

public class JmsMessageListener implements javax.jms.MessageListener {
	private final String subscriptionName;
	
	public JmsMessageListener(String subscriptionName)
	{
		this.subscriptionName = subscriptionName;
	}
	
	public void onMessage(Message message) {
		try {
			Log.receivedMessage(subscriptionName, message);
			message.acknowledge();
		} catch (JMSException exception) {
			exception.printStackTrace();
		}
	}
}
