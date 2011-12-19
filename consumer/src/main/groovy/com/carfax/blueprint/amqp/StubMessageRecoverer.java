package com.carfax.blueprint.amqp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;

public class StubMessageRecoverer implements MessageRecoverer{
	@Autowired
	private AmqpTemplate amqpTemplate;
	private static final Logger LOGGER = LoggerFactory.getLogger(StubMessageRecoverer.class);
	public void recover(Message message, Throwable cause) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		cause.printStackTrace(new PrintStream(out));
		message.getMessageProperties().getHeaders().put("x-exception", out.toString());
		amqpTemplate.send("errors", message.getMessageProperties().getReceivedRoutingKey(), message);
		LOGGER.info("message received and republished");
	}
	

}
