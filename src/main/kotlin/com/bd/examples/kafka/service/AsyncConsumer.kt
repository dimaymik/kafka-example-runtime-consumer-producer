package com.bd.examples.kafka.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

class AsyncConsumer(private val consumer: KafkaConsumer<String, String>) {
    private val closed: AtomicBoolean = AtomicBoolean(false)

    fun subscribe(topic: String, onMessage: (ConsumerRecord<String, String>) -> Unit) {
        GlobalScope.launch {
            try {
                consumer.subscribe(listOf(topic))
                while (!closed.get()) {
                    consumer.poll(Duration.ofMillis(1000)).forEach {
                        onMessage(it)
                        consumer.commitSync()
                    }
                }
            } catch (e: WakeupException) {
                if (!closed.get())
                    throw e
            } finally {
                consumer.close()
            }
        }
    }

    fun shutdown() {
        closed.set(true)
        consumer.wakeup()
    }
}
