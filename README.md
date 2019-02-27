# Spring Kafka Example
### Description
This repository contains an example of using Kafka producers and consumers in Spring web application. It shows how to rise up and shutdown consumers and producers dynamically during application runtime. These producers and consumers can be bound to user-defined Kafka brokers and topics.

### Usage scenarios
##### Sending message
1. Create producer (POST /producer)

   *Provide arbitrary producer id and list of broker addresses*
   
2. Send message (POST /message/send)
   
   *Provide created producer id, target topic and message body*

##### Receiving messages
1. Create consumer (POST /consumer)

   *Provide arbitrary consumer id, topic, list of broker addresses and consumer group*
   
2. Wait for messages to appear in the topic
3. Get list of messages received by the consumer (GET /message/receive/\<consumer id>)
