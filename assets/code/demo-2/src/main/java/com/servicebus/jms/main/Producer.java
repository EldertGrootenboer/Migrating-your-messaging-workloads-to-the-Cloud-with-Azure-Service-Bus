package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.Log;

import javax.jms.BytesMessage;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Producer {
	public static void main(final String[] args) {
		new Producer().runProducer();
	}

	public void runProducer() {
		String conference = "NDC Porto";
		String topicName = "OrderTopic";
		long deliveryDelayInSeconds = 10;
		int largeMessageSizeMB = 10;
		JMSContext jmsContext = null;
		JMSProducer jmsProducer;
		Message message;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);
			jmsProducer = jmsContext.createProducer();

//			Log.section("1. Send general message to topic");
//			message = jmsContext.createTextMessage(String.format("Hello %s!", conference));
//			jmsProducer.send(topic, message);
//			Log.sentMessage(topic.getTopicName(), message);

//			Log.section("2. Send messages for warehouses");
//			message = jmsContext.createTextMessage("Order to be delivered by NA warehouse");
//			message.setStringProperty("Region", "NorthAmerica");
//			jmsProducer.send(topic, message);
//			Log.sentMessage(topic.getTopicName(), message);
////
//			message = jmsContext.createTextMessage("Order to be delivered by EU warehouse");
//			message.setStringProperty("Region", "Europe");
//			jmsProducer.send(topic, message);
//			Log.sentMessage(topic.getTopicName(), message);

//			Log.section("3. Send delayed message for NA warehouse");
//			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//			String timestamp = formatter.format(new Date());
//
//			jmsProducer.setDeliveryDelay(deliveryDelayInSeconds * 1000);
//			message = jmsContext.createTextMessage(
//					String.format("Order to be delivered by NA warehouse, sent at %s, to be handled in %d seconds",
//							timestamp, deliveryDelayInSeconds));
//			message.setStringProperty("Region", "NorthAmerica");
//			jmsProducer.send(topic, message);
//			Log.sentMessage(topic.getTopicName(), message);

//			Log.section("4. Send message and wait for response");
//			Queue temporaryQueue = jmsContext.createTemporaryQueue();
//			message = jmsContext.createTextMessage("Please acknowledge receipt of this message.");
//			message.setStringProperty("Pattern", "RequestResponse");
//			jmsProducer.setDeliveryDelay(deliveryDelayInSeconds * 1000);
//			jmsProducer.setJMSReplyTo(temporaryQueue);
//			jmsProducer.send(topic, message);
//			Log.sentMessage(topic.getTopicName(), message);
//
//			Log.section("Wait for response message");
//			JMSConsumer responseConsumer = jmsContext.createConsumer(temporaryQueue);
//			Message response = responseConsumer.receive();
//			Log.receivedMessage(temporaryQueue.getQueueName(), response);
//			response.acknowledge();

			Log.section("5. Send large message to topic");
			Log.step("Creating large file");
			File inputFile = new File("large_file_jms.dat");
			createFile(inputFile, largeMessageSizeMB * 1024 * 1024);
			Log.step("File created");
//
			BytesMessage byteMessage = jmsContext.createBytesMessage();
			try (BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(inputFile))) {
				byteMessage.writeBytes(bufferedInput.readAllBytes());
				byteMessage.setStringProperty("Pattern", "LargeMessage");
			}

			jmsProducer = jmsContext.createProducer();
			Log.step("Sending large message");
			jmsProducer.send(topic, byteMessage);
			Log.sentMessage(topic.getTopicName(), byteMessage);

		} catch (Exception e) {
			throw new RuntimeException("Error occurred running producer. " + e);
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}

	/**
	 * Creates a file with given size.
	 *
	 * @param file File to write to.
	 * @param fileSize Size of file.
	 * @throws UncheckedIOException if {@code file} could not be found or the output stream could not be closed.
	 */
	private static void createFile(final File file, final long fileSize) {
		try (BufferedOutputStream buffOut = new BufferedOutputStream(new FileOutputStream(file))) {
			byte[] outBuffer = new byte[1024 * 1024];
			for (long i = 0; i < fileSize; i += outBuffer.length) {
				buffOut.write(outBuffer);
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}