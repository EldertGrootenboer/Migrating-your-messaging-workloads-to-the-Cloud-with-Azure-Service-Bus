package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.Log;

import javax.jms.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProducerDemo3ScheduledMessage {
	public static void main(final String[] args) {
		new ProducerDemo3ScheduledMessage().runProducer();
	}

	public void runProducer() {
		String topicName = "OrderTopic";
		long deliveryDelayInSeconds = 10;
		JMSContext jmsContext = null;
		JMSProducer jmsProducer;
		Message message;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);
			jmsProducer = jmsContext.createProducer();

			Log.section("3. Send delayed message for NA warehouse");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String timestamp = formatter.format(new Date());

			jmsProducer.setDeliveryDelay(deliveryDelayInSeconds * 1000);
			message = jmsContext.createTextMessage(
					String.format("Order to be delivered by NA warehouse, sent at %s, to be handled in %d seconds",
							timestamp, deliveryDelayInSeconds));
			message.setStringProperty("Region", "NorthAmerica");
			jmsProducer.send(topic, message);
			Log.sentMessage(topic.getTopicName(), message);

		} catch (Exception e) {
			throw new RuntimeException("Error occurred running producer. " + e);
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}