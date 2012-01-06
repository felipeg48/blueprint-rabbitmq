package com.carfax.blueprint.amqp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:producer-context.xml"})
public class LocalMessageStoreTest {
   @Autowired
   LocalMessageStore localmessageStore;
   @Autowired
   JsonMessageConverter converter;
   
   
   @Test
   public void shouldKeepHeadersIntact(){
      TheDude dude = new TheDude("Lebowski");
      Message message = converter.toMessage(dude, new MessageProperties());
      String originalTypeId = (String) message.getMessageProperties().getHeaders().get("__TypeId__");

      
      localmessageStore.add(new UnsentMessage("a.b.c", "exchng", message));
      
      UnsentMessage unsentMessage = localmessageStore.poll();
      String typeId = (String) unsentMessage.getMessage().getMessageProperties().getHeaders().get("__TypeId__");
      
      assertEquals(originalTypeId, typeId);
   }

   @Test
   public void shouldReturnNullWhenCacheIsEmpty(){
      localmessageStore.add(new UnsentMessage("a.b.c", "exchng", converter.toMessage(new TheDude("Lebowski"), new MessageProperties())));
      localmessageStore.add(new UnsentMessage("a.b.c", "exchng", converter.toMessage(new TheDude("Lebowski"), new MessageProperties())));
      
      assertNotNull(localmessageStore.poll());
      assertNotNull(localmessageStore.poll());
      assertNull(localmessageStore.poll());
   }
   public static class TheDude{
      private String name;

      public TheDude(String name) {
         super();
         this.name = name;
      }

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }
   }
}
