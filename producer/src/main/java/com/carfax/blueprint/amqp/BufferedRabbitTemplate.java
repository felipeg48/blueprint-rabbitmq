package com.carfax.blueprint.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class BufferedRabbitTemplate extends RabbitTemplate {
	private static final Logger LOG = LoggerFactory.getLogger(BufferedRabbitTemplate.class);
	@Autowired
	private MessageMetricsTracker tracker;
	private LocalMessageStore failOverQueue;


	@Override
	public void send(String exchange, String routingKey, Message message)
			throws AmqpException {
		try{
		   LOG.info("Send to broker? {}", tracker.sendToBroker());
		   if(tracker.sendToBroker()){
		      LOG.info("publishing");
		      super.send(exchange, routingKey, message);
		      tracker.messageSent();		      
		   }else{
		      LOG.info("buffering");
		      failOverQueue.add(routingKey, exchange, message);
		   }
		}catch(AmqpConnectException e){
			failOverQueue.add(routingKey, exchange, message);
			
		}
	}


	public void setFailoverQueue(LocalMessageStore queue) {
		failOverQueue = queue;
		
	}
	
}
