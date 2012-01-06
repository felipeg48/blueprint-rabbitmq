package com.carfax.blueprint.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;
@Component
public class LoggingErrorHandler implements ErrorHandler {
   private static final Logger LOG = LoggerFactory.getLogger(LoggingErrorHandler.class);
   public void handleError(Throwable t) {
      LOG.error(t.getMessage(), t);
      
   }

}
