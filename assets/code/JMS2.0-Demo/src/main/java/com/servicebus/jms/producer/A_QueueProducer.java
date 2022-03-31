package com.servicebus.jms.producer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

import com.servicebus.jms.utils.Log;

public class A_QueueProducer implements Runnable {
	private ConnectionFactory connectionFactory;
	private Queue queue;

	public A_QueueProducer(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			jmsContext = connectionFactory.createContext();
			
			queue = jmsContext.createQueue("A_DemoQueue");
			
			JMSProducer jmsProducer = jmsContext.createProducer();
			
			String message = "Hello World!";
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