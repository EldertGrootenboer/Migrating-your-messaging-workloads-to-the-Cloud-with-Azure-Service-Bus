package com.servicebus.jms.consumer;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;
import com.servicebus.jms.utils.Log;

public class E_ScheduledMessageQueueConsumer implements Runnable {
	private Destination destination;
	private ConnectionFactory connectionFactory;

	public E_ScheduledMessageQueueConsumer(Destination destination, ConnectionFactory connectionFactory) {
		this.destination = destination;
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			jmsContext = connectionFactory.createContext();
			JMSConsumer jmsConsumer = jmsContext.createConsumer(destination);

			Log.Step("Waiting for scheduled message");
			Message message = jmsConsumer.receive();
			Log.ReceivedMessage(((Queue)destination).getQueueName(), message);
			message.acknowledge();
		} catch (Exception excep) {
			excep.printStackTrace();
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}