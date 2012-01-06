package com.carfax.blueprint.amqp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.amqp.core.MessageProperties;


public class SerializableMessagePropertiesTest {

   @Test
   public void shouldKeepHeadersIntact(){
      MessageProperties props = new MessageProperties();
      props.getHeaders().put("type", "Awesome");
      
      SerializableMessageProperties properties = SerializableMessageProperties.from(props);
      
      assertEquals("Awesome", props.getHeaders().get("type"));
   }
}
