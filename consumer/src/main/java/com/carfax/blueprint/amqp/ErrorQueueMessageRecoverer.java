package com.carfax.blueprint.amqp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;

public class ErrorQueueMessageRecoverer implements MessageRecoverer {
	@Resource
	AmqpTemplate errorTemplate;

	public void recover(Message message, Throwable cause) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		cause.printStackTrace(new PrintStream(byteArrayOutputStream));
		message.getMessageProperties().getHeaders()
				.put("x-exception", byteArrayOutputStream.toString());
		errorTemplate.send(message);
	}

}
