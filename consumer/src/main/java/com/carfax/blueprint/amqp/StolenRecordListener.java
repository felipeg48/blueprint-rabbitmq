package com.carfax.blueprint.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StolenRecordListener implements VehicleChangeListener{
	private static final Logger LOG = LoggerFactory.getLogger(StolenRecordListener.class);
	public void handleMessage(final Vehicle vehicle){
		throw new org.apache.commons.lang.NotImplementedException();
//		LOG.info("Stolen Record: " + vehicle.toString());
	}

}
