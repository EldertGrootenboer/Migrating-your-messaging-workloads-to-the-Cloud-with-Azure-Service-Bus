package com.servicebus.jms.main;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.servicebus.jms.utils.JmsConnection;

public class Consumer {

	public static void main(String[] args) {
		try {
			JmsConnection jmsConnection = new JmsConnection();
			Connection connection = jmsConnection.GetConnection();
			Destination queue = jmsConnection.GetDestination();
			
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = session.createConsumer(queue);
			
			Message message = consumer.receive(1000);

			String text = ((TextMessage) message).getText();
			System.out.println("Consumer Received: " + text);

			session.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
