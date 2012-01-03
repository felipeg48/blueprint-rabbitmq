package com.carfax.blueprint.amqp;


import groovy.util.logging.Slf4j

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.config.StatefulRetryOperationsInterceptorFactoryBean
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.amqp.support.converter.JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.ImportResource
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

@Slf4j
@Configuration
@ImportResource("classpath:com/carfax/blueprint/amqp/jndi-context.xml")
public class ApplicationConfig {
	@Autowired
	ConnectionFactory amqpConnectionFactory;
	@Autowired
	StubMessageRecoverer messageRecoverer;
	
	@Bean
	Queue queue(){
		return new Queue("vehicle.changes");
	}
	@Bean
	Queue errorQueue(){
		return new Queue("vehicle.errors");
	}
	@Bean
	RabbitAdmin rabbitAdmin(){
		return new RabbitAdmin(amqpConnectionFactory);
	}
	@Bean
	RabbitTemplate rabbitPublisher(){
		return new RabbitTemplate(amqpConnectionFactory);
	}
	@Bean
	TopicExchange exchange(){
		return new TopicExchange("errors");
	}
	@Bean
	public Binding errorBinding(){
		return BindingBuilder.bind(errorQueue()).to(exchange()).with("vehicle.changes");
	}
	@Bean
	public SimpleMessageListenerContainer container(){
		SimpleMessageListenerContainer container= new SimpleMessageListenerContainer(amqpConnectionFactory);
		container.setQueueNames("vehicle.changes");
		container.setAutoStartup(true);
		container.setConcurrentConsumers(2);
		container.setMessageListener(messageListener());
		container.setErrorHandler(new LoggingErrorHandler(log));
		container.adviceChain= [retryHandler()]
		
		return container;
	}
	
	@Bean
	public SimpleMessageListenerContainer errorListenerContainer(){
		SimpleMessageListenerContainer container= new SimpleMessageListenerContainer(amqpConnectionFactory);
		container.setQueueNames("vehicle.errors");
		container.setAutoStartup(true);
		container.setConcurrentConsumers(2);
		container.setMessageListener(errorMessageListener());
		container.setErrorHandler(new LoggingErrorHandler(log));
		return container;
	}
	@Bean
	public MessageListener errorMessageListener() {
		return new ErrorMessageListener();
	}
	@Bean
	public StatefulRetryOperationsInterceptor retryHandler() {
		StatefulRetryOperationsInterceptorFactoryBean retryOperationsInterceptor = new StatefulRetryOperationsInterceptorFactoryBean();
		retryOperationsInterceptor.setRetryOperations(retryTemplate());
		retryOperationsInterceptor.setMessageRecoverer(messageRecoverer);
		return retryOperationsInterceptor.getObject();
	}
	@Bean
	@DependsOn("container")
	MBeanExporterPostProcessor exporter(){
		def mbeans = new MBeanExporterPostProcessor(namespace:"com.carfax.amqp")
		mbeans.setBeanType(SimpleMessageListenerContainer, Queue)
		mbeans
	}
	@Bean
	public StubMessageRecoverer stubMessageRecoverer() {
		return new StubMessageRecoverer();
	}
	private RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(retryPolicy());
		retryTemplate.setBackOffPolicy(backOffPolicy());
		return retryTemplate;
	}
	private FixedBackOffPolicy backOffPolicy() {
		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(5000);
		return fixedBackOffPolicy;
	}
	private SimpleRetryPolicy retryPolicy() {
		SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
		simpleRetryPolicy.setMaxAttempts(3);
		return simpleRetryPolicy;
	}
	@Bean
	public MessageListener messageListener() {
		return new MessageListenerAdapter(new VehicleChangeListener(), new JsonMessageConverter());
	}
	

}
