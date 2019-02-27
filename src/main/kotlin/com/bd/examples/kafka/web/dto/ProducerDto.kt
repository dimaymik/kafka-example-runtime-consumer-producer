package com.bd.examples.kafka.web.dto

data class ProducerDto(val id: String,
                       val topic: String,
                       val bootstrapServers: List<String>)
