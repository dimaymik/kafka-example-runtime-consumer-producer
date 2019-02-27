package com.bd.examples.kafka.web.dto

data class SentMessageDto(val producerId: String,
                          val topic: String,
                          val body: String)

data class ReceivedMessageDto(val consumerId: String,
                              val body: String)
