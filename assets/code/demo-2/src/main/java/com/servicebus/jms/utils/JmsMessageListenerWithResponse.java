package com.servicebus.jms.utils;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

public class JmsMessageListenerWithResponse implements javax.jms.MessageListener {
	private final String subscriptionName;
	private final JMSContext jmsContext;
	
	public JmsMessageListenerWithResponse(String subscriptionName, JMSContext jmsContext) {
		this.subscriptionName = subscriptionName;
		this.jmsContext = jmsContext;
	}
	
	public void onMessage(Message message) {
		try {
			Log.receivedMessage(subscriptionName, message);
			message.acknowledge();
			
			Destination responseQueue = message.getJMSReplyTo();
			JMSProducer producer = jmsContext.createProducer();
			String messageToSend = "Message receive acknowledged";
			producer.send(responseQueue, messageToSend);
			Log.sentMessage(((Queue)responseQueue).getQueueName(), messageToSend);
		} catch (JMSException exception) {
			exception.printStackTrace();
		}
	}
}
