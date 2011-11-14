# RabbitMQ Blueprint Project
This is a blueprint project illustrating how to use rabbitMQ on the JVM. It
uses a multi-module project setup and will have branches dedicated to different
topics (different exchange types, messaging patterns, within a web container,
etc).

## Checking Out the Examples
The master branch has two tagged examples that are meant to be taken as a
starting point to understanding spring-amqp. 

For an example of a simple message listener with no marshalling, run:

    git checkout simple

For an example of using MessageListenerAdapter to have a POJO receive a message
as a java object run:
  
    git checkout message-listener-adapter

## Running It
Whenever you check out a tag or branch the first thing you should do is run 

    gradle eclipse

Fromt he root of the project. Then from eclipse import the directory and it
should bring all of the subprojects in to your workspace.

Running the ApplicationConfig from the consumer will start the consumer up,
running the same class from the producer will start the producer up and publish
some test messages.

Have fun!

## Topic Branches
* webcontainer-example - example of using rabbitMQ within a webcontainer
* webcontainer-example-jndi - same as above, but with the connection factory provided via jndi
* topic-exchange-example - uses a topic exchange with a queue bound to the
exchange
* topic-exchange-example-multiple-queues - illustrates using multiple queues
with a single exchange
* xml-based-config - an example using XML instead of Java based Spring Configuration
* dead-letter-example - An example of implementing a dead letter queue (http://eaipatterns.com/DeadLetterChannel.html) using an Advice in the SimpleMessageListenerContainer
