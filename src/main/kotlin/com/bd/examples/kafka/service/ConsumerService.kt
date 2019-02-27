package com.bd.examples.kafka.service

import com.bd.examples.kafka.exceptions.AlreadyExistsException
import com.bd.examples.kafka.exceptions.NotExistException
import com.bd.examples.kafka.web.dto.ConsumerDto
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PreDestroy

@Service
class ConsumerService {
    private val consumers = mutableMapOf<String, AsyncConsumer>()
    private val messages = mutableMapOf<String, MutableList<String>>()

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ConsumerService::class.java)
    }

    fun createConsumer(request: ConsumerDto) {
        if (consumers.containsKey(request.id))
            throw AlreadyExistsException("Consumer '${request.id}' already exists")
        val props = Properties()
        props["bootstrap.servers"] = request.bootstrapServers.joinToString()
        props["group.id"] = request.group
        props["enable.auto.commit"] = false
        props["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        props["value.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        val consumer = AsyncConsumer(KafkaConsumer(props))
        consumers[request.id] = consumer
        messages[request.id] = mutableListOf()
        consumer.subscribe(request.topic) {
            log.debug("Got message: $it - ${it.value()}")
            messages[request.id]?.add(it.value())
        }
    }

    fun closeConsumer(id: String) {
        consumers[id]?.also {
            it.shutdown()
            consumers.remove(id)
        } ?: throw NotExistException("Consumer '$id' does not exist")
    }

    fun getMessages(consumerId: String): List<String> = messages[consumerId]
            ?: throw NotExistException("Consumer '$consumerId' does not exist")

    @PreDestroy
    fun closeAll() {
        consumers.values.forEach { it.shutdown() }
    }
}
