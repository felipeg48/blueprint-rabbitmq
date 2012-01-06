package com.carfax.blueprint.amqp;

import java.io.Serializable;

import org.springframework.amqp.core.Message;

public class SerializableMessage implements Serializable{
   private final SerializableMessageProperties messageProperties;
   private final byte[] body;

   public SerializableMessage(Message message) {
      this.body = message.getBody();
      this.messageProperties = SerializableMessageProperties.from(message.getMessageProperties());
   }
   
   public Message toMessage(){
      return new Message(body, messageProperties.toMessageProperties());
   }
}
