package com.carfax.blueprint.amqp;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

@Configuration
@ImportResource("classpath:producer-context.xml")
@ComponentScan("com.carfax.blueprint.amqp")
public class ApplicationConfig {
	private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);
	
	@Autowired
	ConnectionListener unsentMessageHandler;
	@Autowired
	ConnectionFactory connectionFactory;
	@Autowired
	MBeanServer mbeanServer;
	@PostConstruct
	public void doAfter(){
		connectionFactory.addConnectionListener(unsentMessageHandler);
		
		log.info("There are {} mbeans", mbeanServer.getMBeanCount());

	}
	
	
	@Bean(autowire=Autowire.BY_NAME)
	HistoryProcessor processor(){
		return new HistoryProcessor();
	}
	
	@Bean
	public MBeanExporterPostProcessor exporter(){
	   MBeanExporterPostProcessor exporter = new MBeanExporterPostProcessor();
	   exporter.setBeanType(LocalMessageStore.class, MessageMetricsTracker.class);
	   exporter.setNamespace("com.carfax.blueprint.amqp");
	   exporter.setServer(mbeanServer);
      return exporter;
	}
	@Bean
	VehicleSource vehicleSource(){
		VehicleSource source = new VehicleSource();
		
		source.add(newVehicle("Toyota", "Tercel", "1995"));
		source.add(newVehicle("Ford", "Mustang", "2008"));
		source.add(newVehicle("Honda", "Prelude", "1985"));
		source.add(newVehicle("Honda", "Civic", "1999"));
		source.add(newVehicle("Nissan", "Altima", "2003"));
		return source;
	}
	
	private String pad(String string) {
		StringBuilder s = new StringBuilder(string);
		for(int i = 0; i < 50000; i++){
			s.append(string);
		}
		return s.toString();
	}


	@Bean
	BeanPostProcessor postProcessor(){
		return new ScheduledAnnotationBeanPostProcessor();
	}
	public static void main(String... args){
		new AnnotationConfigApplicationContext(ApplicationConfig.class);
	}
	private Vehicle newVehicle(String make, String model, String year) {
		Vehicle vehicle = new Vehicle();
		vehicle.setMake(make);
		vehicle.setModel(model);
		vehicle.setYear(year);
		return vehicle;
	}
}
