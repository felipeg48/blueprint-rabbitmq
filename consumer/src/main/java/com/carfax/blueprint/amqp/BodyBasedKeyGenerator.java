package com.carfax.blueprint.amqp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageKeyGenerator;

public class BodyBasedKeyGenerator implements MessageKeyGenerator {

	public Object getKey(Message message) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(message.getBody());
			return new String(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
