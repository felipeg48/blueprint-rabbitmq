package com.carfax.blueprint.amqp;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.amqp.core.Message;
import org.springframework.scheduling.annotation.Async;

public class LocalMessageStore {
   private Cache cache;
   public void setCache(Cache cache) {
      this.cache = cache;
   }
   public void add(UnsentMessage unsentMessage) {
      cache.put(new Element(unsentMessage.getId(), unsentMessage));
      
   }
   public UnsentMessage poll(){
      List keysWithExpiryCheck = cache.getKeysWithExpiryCheck();
      if(keysWithExpiryCheck.size() > 0){
         return (UnsentMessage) cache.removeAndReturnElement(keysWithExpiryCheck.get(0)).getValue();         
      }
      return null;
   }
   public boolean isEmpty(){
      return cache.getKeysWithExpiryCheck().size() == 0;
   }
   public int size() {
      return cache.getKeysWithExpiryCheck().size();
   }
   public void add(String routingKey, String exchange, Message message) {
      add(new UnsentMessage(routingKey, exchange, message));
      
   }
}
