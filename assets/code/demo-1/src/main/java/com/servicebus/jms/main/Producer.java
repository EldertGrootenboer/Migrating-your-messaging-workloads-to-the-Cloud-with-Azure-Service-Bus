package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnection;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import java.io.IOException;

public class Producer {

	public static void main(String[] args) throws NamingException, JMSException, IOException {
		JmsConnection jmsConnection = new JmsConnection();
		Connection connection = null;
		Session session = null;

		try {
			connection = jmsConnection.getConnection();
			Destination queue = jmsConnection.getDestination();

			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageProducer producer = session.createProducer(queue);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			TextMessage message = session.createTextMessage("Hello World");
			producer.send(message);

			System.out.println("Producer Sent: " + message.getText());
		} finally {
			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}
