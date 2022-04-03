package com.servicebus.jms.producer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import com.servicebus.jms.utils.Log;

public class C_TemporaryQueueProducer implements Runnable {
	private ConnectionFactory connectionFactory;
	private Queue queue;

	public C_TemporaryQueueProducer(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			jmsContext = connectionFactory.createContext();
			
			Queue temporaryQueue = jmsContext.createTemporaryQueue();
			
			queue = jmsContext.createQueue("C_RequestResponseQueue");
						
			JMSProducer jmsProducer = jmsContext.createProducer();
			jmsProducer.setJMSReplyTo(temporaryQueue);
			
			String message = "Hello World!";
			jmsProducer.send(queue, message);
			Log.SentMessage(queue.getQueueName(), message);
			
			JMSConsumer responseConsumer = jmsContext.createConsumer(temporaryQueue);
			Message response = responseConsumer.receive();
			Log.ReceivedMessage(temporaryQueue.getQueueName(), response);
			response.acknowledge();
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