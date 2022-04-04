package com.servicebus.jms.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.jms.BytesMessage;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import com.servicebus.jms.utils.JmsTopic;
import com.servicebus.jms.utils.Log;


import javax.jms.ConnectionFactory;

public class Producer {
	public static void main(final String[] args) throws Exception {
		new Producer().runProducer();
	}

	public void runProducer() throws Exception {
		String conference = "NDC Porto";
		String topicName = "OrderTopic";
		long deliveryDelayInSeconds = 10;
		int largeMessageSizeMB = 10;
		JMSContext jmsContext = null;
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		String connectionString = properties.getProperty("jms.connectionstring");

		// ActiveMQ
		ActiveMQJMSConnectionFactory connectionFactory = new ActiveMQJMSConnectionFactory(connectionString);

		// Service Bus
//			ServiceBusJmsConnectionFactorySettings connFactorySettings = new ServiceBusJmsConnectionFactorySettings();
//			connFactorySettings.setConnectionIdleTimeoutMS(20000);
//			ServiceBusJmsConnectionFactory connectionFactory = new ServiceBusJmsConnectionFactory(connectionString, connFactorySettings);

		try {
			jmsContext = connectionFactory.createContext();
			Topic topic = JmsTopic.Get(connectionFactory, topicName);
			Message message = null;
			JMSProducer jmsProducer = jmsContext.createProducer();
			
			

			Log.Section("Send general message to topic");			
			message = jmsContext.createTextMessage(String.format("Hello %s!", conference));		
			jmsProducer.send(topic, message);
			Log.SentMessage(topic.getTopicName(), message);
			


			Log.Section("Send messages for warehouses");
			message = jmsContext.createTextMessage("Order to be delivered by NA warehouse");
			message.setStringProperty("Region", "NorthAmerica");		
			jmsProducer.send(topic, message);
			Log.SentMessage(topic.getTopicName(), message);
			
			message = jmsContext.createTextMessage("Order to be delivered by EU warehouse");
			message.setStringProperty("Region", "NorthAmerica");
			jmsProducer.send(topic, message);
			Log.SentMessage(topic.getTopicName(), message);
			


			Log.Section("Send delayed message for NA warehouse");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String timestamp = formatter.format(new Date());
			
			jmsProducer.setDeliveryDelay(deliveryDelayInSeconds * 1000);
			message = jmsContext.createTextMessage(String.format("Order to be delivered by NA warehouse, sent at %s, to be handled in %d seconds", timestamp, deliveryDelayInSeconds));
			message.setStringProperty("Region", "NorthAmerica");		
			jmsProducer.send(topic, message);
			Log.SentMessage(topic.getTopicName(), message);
			
			
			
			Log.Section("Send message and wait for response");
			Queue temporaryQueue = jmsContext.createTemporaryQueue();
			message = jmsContext.createTextMessage("Please acknowledge receipt of this message.");
			message.setStringProperty("Pattern", "RequestResponse");
			jmsProducer.setDeliveryDelay(deliveryDelayInSeconds * 1000);
			jmsProducer.setJMSReplyTo(temporaryQueue);
			jmsProducer.send(topic, message);
			Log.SentMessage(topic.getTopicName(), message);

			Log.Section("Wait for response message");
			JMSConsumer responseConsumer = jmsContext.createConsumer(temporaryQueue);
			Message response = responseConsumer.receive();
			Log.ReceivedMessage(temporaryQueue.getQueueName(), response);
			response.acknowledge();
			
			
			
			Log.Section("Send large message to topic");
			Log.Step("Creating large file");
			File inputFile = new File("large_file_jms.dat");
			createFile(inputFile, largeMessageSizeMB * 1024 * 1024);
			Log.Step("File created");
			
			BytesMessage byteMessage = jmsContext.createBytesMessage();
			FileInputStream fileInputStream = new FileInputStream(inputFile);
			BufferedInputStream bufferedInput = new BufferedInputStream(fileInputStream);
			byteMessage.writeBytes(bufferedInput.readAllBytes());
			byteMessage.setStringProperty("Pattern", "LargeMessage");
			bufferedInput.close();

			jmsProducer = jmsContext.createProducer();
			Log.Step("Sending large message");
			jmsProducer.send(topic, byteMessage);
			Log.SentMessage(topic.getTopicName(), byteMessage);
			
		} catch (Exception excep) {
			excep.printStackTrace();
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}

	private static void createFile(final File file, final long fileSize) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(file);
		try (BufferedOutputStream buffOut = new BufferedOutputStream(fileOut)) {
			byte[] outBuffer = new byte[1024 * 1024];
			for (long i = 0; i < fileSize; i += outBuffer.length) {
				buffOut.write(outBuffer);
			}
		}
	}
}