package com.bd.examples.kafka.web.rest

import com.bd.examples.kafka.service.ConsumerService
import com.bd.examples.kafka.web.dto.ConsumerDto
import com.bd.examples.kafka.web.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/consumer")
class ConsumerController(private val consumerService: ConsumerService) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ConsumerController::class.java)
    }

    @PostMapping
    fun createConsumer(@RequestBody request: ConsumerDto): ResponseDto {
        log.debug("Request to create consumer '${request.id}'")
        consumerService.createConsumer(request)
        return ResponseDto(0, "Consumer created")
    }

    @DeleteMapping("/{id}")
    fun deleteConsumer(@PathVariable id: String): ResponseDto {
        log.debug("Request to delete consumer '$id'")
        consumerService.closeConsumer(id)
        return ResponseDto(0, "Consumer deleted")
    }
}
