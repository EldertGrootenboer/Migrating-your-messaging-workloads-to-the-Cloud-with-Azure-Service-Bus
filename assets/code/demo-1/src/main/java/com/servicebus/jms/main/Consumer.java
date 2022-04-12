package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnection;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import java.io.IOException;

public class Consumer {

	public static void main(String[] args) throws NamingException, JMSException, IOException {
		JmsConnection jmsConnection = new JmsConnection();

		try (Connection connection = jmsConnection.getConnection()) {
			Destination queue = jmsConnection.getDestination();

			connection.start();

			try (Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
				MessageConsumer consumer = session.createConsumer(queue);

				Message message = consumer.receive(1000);

				String text = ((TextMessage) message).getText();
				System.out.println("Consumer Received: " + text);
			}
		}
	}
}
