package com.carfax.blueprint.amqp;

import org.springframework.amqp.core.Message;

public class MessageError {

	private final Throwable cause;
	private final Message message;

	public MessageError(Message message, Throwable cause) {
		this.message = message;
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}

	public Message getMessage() {
		return message;
	}

}
