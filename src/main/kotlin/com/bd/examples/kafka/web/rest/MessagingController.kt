package com.bd.examples.kafka.web.rest

import com.bd.examples.kafka.service.ConsumerService
import com.bd.examples.kafka.service.ProducerService
import com.bd.examples.kafka.web.dto.ReceivedMessageDto
import com.bd.examples.kafka.web.dto.SentMessageDto
import com.bd.examples.kafka.web.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/message")
class MessagingController(private val producerService: ProducerService,
                          private val consumerService: ConsumerService) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MessagingController::class.java)
    }

    @PostMapping("/send")
    fun sendMessage(@RequestBody request: SentMessageDto): ResponseDto {
        log.debug("Request to send message '${request.body}' to topic '${request.topic}'")
        producerService.sendMessage(request)
        return ResponseDto(0, "Message sent")
    }

    @GetMapping("/receive/{consumerId}")
    fun receiveMessages(@PathVariable consumerId: String): List<ReceivedMessageDto> {
        log.debug("Request to receive messages from consumer '$consumerId'")
        return consumerService.getMessages(consumerId).map { ReceivedMessageDto(consumerId, it) }
    }
}
