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
All of these examples use [gradle](http://www.gradle.org) to build. 

Whenever you check out a tag or branch the first thing you should do (if running eclipse) is run 

    gradle eclipse

from the root of the project. Then from eclipse import the directory and it
should bring all of the subprojects in to your workspace.

### Non-Webcontainer Examples
Running the ApplicationConfig from the consumer will start the consumer up,
running the same class from the producer will start the producer up and publish
some test messages.

### WebContainer Examples
Most of the webcontainer based examples use jettyRun to run. Always run gradle tasks from the root of the directory to double check. To run the consumer in this case you'd run

    gradle consumer:jettyRun
    
from the commandline to start it up. For those most part these kind of examples tend to use JNDI.

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
                    
## Installing RabbitMQ
Don't have rabbitMQ installed? Either change the host to dev.rabbitmq.com for the public rabbitMQ broker or follow the instructions on the site for [windows](http://www.rabbitmq.com/install.html#windows) or [linux](http://www.rabbitmq.com/install.html#rpm).
