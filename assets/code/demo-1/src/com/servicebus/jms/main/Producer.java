package com.servicebus.jms.main;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import com.servicebus.jms.utils.JmsConnection;

public class Producer {

	public static void main(String[] args) {
		try {
			JmsConnection jmsConnection = new JmsConnection();
			Connection connection = jmsConnection.GetConnection();
			Destination queue = jmsConnection.GetDestination();
			
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageProducer producer = session.createProducer(queue);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			TextMessage message = session.createTextMessage("Hello World");
			producer.send(message);
			System.out.println("Producer Sent: " + message.getText());

			session.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
