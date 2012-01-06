package com.carfax.blueprint.amqp;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jmx.export.MBeanExporter;



public class MBeanExporterPostProcessor extends MBeanExporter implements ApplicationContextAware{
   private Logger log = LoggerFactory.getLogger(MBeanExporterPostProcessor.class);
   private ApplicationContext applicationContext;
   private String namespace;
   private Class<?>[] beanTypes;

   public void setBeanType(Class<?>... clazz) {
      this.beanTypes = clazz;
   }
   
   
   public void setNamespace(String namespace) {
      this.namespace = namespace;
   }


   @Override
   public void registerBeans() {
      Map<String, Object> map = new HashMap<String, Object>();
      for(Class<?> beanType : beanTypes){
         Map<String, ?> beans = applicationContext.getBeansOfType(beanType);
         log.info("bean count is " + beans.size());
         for(Entry<String, ?> entry: beans.entrySet()){
            map.put(namespace+":name="+entry.getKey(), entry.getValue());
            log.info("Adding "+namespace+":name="+entry.getKey()+" to mbeans");
         }        
      }
      setBeans(map);
      super.registerBeans();
   }


   public void setApplicationContext(ApplicationContext applicationContext)
         throws BeansException {

      this.applicationContext = applicationContext;

   }
}
