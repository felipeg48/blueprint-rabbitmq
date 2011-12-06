# RabbitMQ Blueprint Project: Error Handling
This branch illustrates how to use spring-amqp's hidden features to achieve 
pragmatic error handling. It makes use of StatefulRetryOperationsInterceptor
to configure the listener to retry a failed message every 5 seconds and on the 
3rd failure republishes the message to an error exchange with the exception
stacktrace in a new header field named x-exception.

## Running it
Whenever you check out a tag or branch the first thing you should do is run 

    gradle eclipse
or
    gradle idea

From the root of the project to build the project files for your preferred IDE.

Running the consumer:
    gradle consumer:jettyRun
    
Running the producer requires you to run the AppConfig class from your IDE (for now) to publish a message.