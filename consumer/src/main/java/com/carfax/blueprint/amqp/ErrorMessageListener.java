package com.carfax.blueprint.amqp;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class ErrorMessageListener implements MessageListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMessageListener.class);
	public void onMessage(Message message) {
		LOGGER.info("Got error'd message");
		LOGGER.info("Body: "+ new String(message.getBody()));
		LOGGER.info("Exception message:");
		LOGGER.info("----------------------------------");
		readException(message);
		LOGGER.info("----------------------------------");
		
	}
	private void readException(Message message) {
		DataInputStream in = (DataInputStream) message.getMessageProperties().getHeaders().get("x-exception");
		
		try {
			LOGGER.info(IOUtils.toString(in));
		} catch (IOException e) {
			LOGGER.error("Error occured in error handler");
			e.printStackTrace();
		}
	}
}
