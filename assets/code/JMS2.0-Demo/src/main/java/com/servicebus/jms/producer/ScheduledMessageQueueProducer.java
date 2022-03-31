package com.servicebus.jms.producer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

import com.servicebus.jms.utils.Log;

public class ScheduledMessageQueueProducer implements Runnable {
	private ConnectionFactory connectionFactory;
	private Queue queue;

	public ScheduledMessageQueueProducer(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			jmsContext = connectionFactory.createContext();
			
			queue = jmsContext.createQueue("jmsScheduledMessageDemoQueue");
			
			JMSProducer jmsProducer = jmsContext.createProducer();

			Message message = jmsContext.createTextMessage("Hello world!");
			message.setJMSDeliveryTime(10000);
			jmsProducer.send(queue, message);
			Log.SentMessage(queue.getQueueName(), message);
		} catch (Exception excep) {
			excep.printStackTrace();
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
	
	public Queue GetQueue() {
		return queue;
	}
}