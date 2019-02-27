package com.bd.examples.kafka.web.rest

import com.bd.examples.kafka.service.ProducerService
import com.bd.examples.kafka.web.dto.ProducerDto
import com.bd.examples.kafka.web.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/producer")
class ProducerController(private val producesService: ProducerService) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ProducerController::class.java)
    }

    @PostMapping
    fun createProducer(@RequestBody request: ProducerDto): ResponseDto {
        log.debug("Request to create producer '${request.topic}'")
        producesService.createProducer(request)
        return ResponseDto(0, "Producer '${request.topic}' created")
    }

    @DeleteMapping("/{topic}")
    fun closeProducer(@PathVariable topic: String): ResponseDto {
        log.debug("Request to close producer '$topic'")
        producesService.closeProducer(topic)
        return ResponseDto(0, "Producer '$topic' closed")
    }
}
