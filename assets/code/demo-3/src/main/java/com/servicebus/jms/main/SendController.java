package com.servicebus.jms.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

	private static final String DESTINATION_NAME = "UserTopic";

	private static final Logger logger = LoggerFactory.getLogger(SendController.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@PostMapping("/users")
	public String postUserMessage(@RequestParam String username) {
		logger.info(String.format("Sending message for user: %s", username));
		jmsTemplate.convertAndSend(DESTINATION_NAME, new User(username), jmsMessage -> {
			jmsMessage.setStringProperty("UserType", "User");
			return jmsMessage;
		});
		return username;
	}

	@PostMapping("/admins")
	public String postAdminMessage(@RequestParam String username) {
		logger.info(String.format("Sending message for admin: %s", username));
		jmsTemplate.convertAndSend(DESTINATION_NAME, new User(username), jmsMessage -> {
			jmsMessage.setStringProperty("UserType", "Admin");
			return jmsMessage;
		});
		return username;
	}
}
