package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.Log;

import javax.jms.*;
import java.io.*;

public class ProducerDemo5LargeMessageSupport {
	public static void main(final String[] args) {
		new ProducerDemo5LargeMessageSupport().runProducer();
	}

	public void runProducer() {
		String topicName = "OrderTopic";
		int largeMessageSizeMB = 10;
		JMSContext jmsContext = null;
		JMSProducer jmsProducer;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);

			Log.section("5. Send large message to topic");
			Log.step("Creating large file");
			File inputFile = new File("large_file_jms.dat");
			createFile(inputFile, largeMessageSizeMB * 1024 * 1024);
			Log.step("File created");

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