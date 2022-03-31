package com.servicebus.jms.consumer;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

import com.servicebus.jms.utils.Log;

public class TemporaryQueueConsumer implements Runnable {
	private Destination destination;
	private ConnectionFactory connectionFactory;

	public TemporaryQueueConsumer(Destination destination, ConnectionFactory connectionFactory) {
		this.destination = destination;
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			jmsContext = connectionFactory.createContext();
			JMSConsumer jmsConsumer = jmsContext.createConsumer(destination);

			Message message = jmsConsumer.receive();
			Log.ReceivedMessage(((Queue)destination).getQueueName(), message);
			message.acknowledge();
			
			Destination responseQueue = message.getJMSReplyTo();
			JMSProducer producer = jmsContext.createProducer();
			String messageToSend = "Response message";
			producer.send(responseQueue, messageToSend);
			Log.SentMessage(((Queue)responseQueue).getQueueName(), messageToSend);
		} catch (Exception excep) {
			excep.printStackTrace();
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}