package com.carfax.blueprint.amqp;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Scheduled;

public class HistoryProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(HistoryProcessor.class);
	private static final AtomicInteger numberSent = new AtomicInteger();
	private AmqpTemplate amqpTemplate;
	private VehicleSource vehicleSource;
	public void setVehicleSource(VehicleSource vehicleSource) {
		this.vehicleSource = vehicleSource;
	}

	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}
	
	@Scheduled(fixedRate=1)
	public void process(){
		Vehicle vehicle = vehicleSource.next();
		if(vehicle != null){
		   for(int i =0; i < 2000; i++){
		      LOGGER.info("Sending message "+numberSent.incrementAndGet());
		      amqpTemplate.convertAndSend(vehicle);		      
		   }
		
		}
		
	}
}
