package com.carfax.blueprint.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;

import sun.misc.resources.Messages_zh_TW;

public class UnsentMessageHandler implements ConnectionListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(UnsentMessageHandler.class);
	private AmqpTemplate amqpTemplate;
	@Autowired
	private MessageMetricsTracker tracker;
	private LocalMessageStore failOverQueue;

	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}

	public void setFailOverQueue(LocalMessageStore failOverQueue) {
		this.failOverQueue = failOverQueue;
	}
	@Async
	public void onCreate(Connection connection) {
		Assert.notNull(amqpTemplate, "amqpTemplate is required");
		Assert.notNull(failOverQueue, "failOverQueue is required");
		if (connection.isOpen() && !failOverQueue.isEmpty()) {
			UnsentMessage unsentMessage = null;
			final int size = failOverQueue.size();
         LOG.info("Connection re-established. Publishing {} unsent messages!", size);
			while ((unsentMessage = failOverQueue.poll()) != null) {
				amqpTemplate.send(unsentMessage.getExchange(),
						unsentMessage.getRoutingKey(),
						unsentMessage.getMessage());
			}
			tracker.sentAfterOutage(size);
		}
	}

	public void onClose(Connection connection) {
	   tracker.disconnect();
	}

}
