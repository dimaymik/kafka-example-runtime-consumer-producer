package com.bd.examples.kafka.web.dto

data class ConsumerDto(val id: String,
                       val topic: String,
                       val bootstrapServers: List<String>,
                       val group: String)
