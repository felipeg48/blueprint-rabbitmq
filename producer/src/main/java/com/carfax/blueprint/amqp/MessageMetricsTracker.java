package com.carfax.blueprint.amqp;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Component;
@Component
public class MessageMetricsTracker {
   private AtomicInteger messagesSent = new AtomicInteger();
   private AtomicInteger disconnects = new AtomicInteger();
   private AtomicInteger messagesResentAfterOutages = new AtomicInteger();
   private boolean sendToBroker = true;
   @ManagedOperation(description="Buffer any published messages, don't send to broker")
   public void bufferMessages(){
      sendToBroker = false;
   }
   @ManagedOperation(description="Publish messages to brokers")
   public void publishMessages(){
      sendToBroker = true;
   }
   
   public boolean sendToBroker() {
      return sendToBroker;
   }
   public void messageSent(){
      messagesSent.incrementAndGet();
   }
   public void disconnect(){
      disconnects.incrementAndGet();
   }
   public void sentAfterOutage(int number){
      messagesResentAfterOutages.addAndGet(number);
   }
   public int getMessagesSent() {
      return messagesSent.get();
   }
   public int getDisconnects() {
      return disconnects.get();
   }
   public int getMessagesResentAfterOutages() {
      return messagesResentAfterOutages.get();
   }
   
   
}
