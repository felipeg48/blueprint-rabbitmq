package com.carfax.blueprint.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;

public class SendToErrorQueueBackOffPolicy implements BackOffPolicy {
	private static final Logger LOG = LoggerFactory.getLogger(SendToErrorQueueBackOffPolicy.class);
	public BackOffContext start(RetryContext context) {
		LOG.info(context.getRetryCount()+"");
		if(context.isExhaustedOnly()){
			LOG.info("Final retry attempt!");
		}
		return null;
	}

	public void backOff(BackOffContext backOffContext)
			throws BackOffInterruptedException {
		
	}

}
