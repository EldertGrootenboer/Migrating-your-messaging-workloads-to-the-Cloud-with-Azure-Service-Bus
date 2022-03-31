package com.servicebus.jms.producer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

import com.servicebus.jms.utils.Log;

public class F_LargeMessageQueueProducer implements Runnable {
	private ConnectionFactory connectionFactory;
	private Queue queue;

	public F_LargeMessageQueueProducer(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			Log.Step("Creating large file");
			File inputFile = new File("large_file_jms.dat");
			createFile(inputFile, 524288000);
			Log.Step("File created");

			jmsContext = connectionFactory.createContext();

			queue = jmsContext.createQueue("F_LargeMessageDemoQueue");

			BytesMessage message = jmsContext.createBytesMessage();
			FileInputStream fileInputStream = new FileInputStream(inputFile);
			BufferedInputStream bufferedInput = new BufferedInputStream(fileInputStream);
			message.setObjectProperty("JMS_AMQ_InputStream", bufferedInput);

			JMSProducer jmsProducer = jmsContext.createProducer();
			Log.Step("Sending large message");
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