package com.bd.examples.kafka.service

import com.bd.examples.kafka.exceptions.AlreadyExistsException
import com.bd.examples.kafka.exceptions.NotExistException
import com.bd.examples.kafka.utils.subscribe
import com.bd.examples.kafka.web.dto.SentMessageDto
import com.bd.examples.kafka.web.dto.ProducerDto
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PreDestroy

@Service
class ProducerService {
    private val producers = mutableMapOf<String, KafkaProducer<String, String>>()

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ProducerService::class.java)
    }

    fun createProducer(request: ProducerDto) {
        if (producers.containsKey(request.id))
            throw AlreadyExistsException("Producer '${request.id}' already exists")
        val props = Properties()
        props["bootstrap.servers"] = request.bootstrapServers.joinToString()
        props["acks"] = "all"
        props["batch.size"] = 16384
        props["request.timeout.ms"] = 10000
        props["linger.ms"] = 0
        props["delivery.timeout.ms"] = 30000
        props["buffer.memory"] = 33554432
        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        producers[request.id] = KafkaProducer(props)
    }

    fun closeProducer(id: String) {
        producers[id]?.let {
            it.close()
            producers.remove(id)
        } ?: throw NotExistException("Producer '$id' does not exist")
    }

    fun sendMessage(request: SentMessageDto) {
        producers[request.producerId]
                ?.send(ProducerRecord(request.topic, request.body))
                ?.subscribe { log.debug("Send result: $it") }
                ?: throw NotExistException("Producer '${request.producerId}' does not exist")
    }

    @PreDestroy
    fun closeAll() {
        producers.values.forEach { it.close() }
    }
}
