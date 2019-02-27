package com.bd.examples.kafka.web.rest

import com.bd.examples.kafka.exceptions.AlreadyExistsException
import com.bd.examples.kafka.exceptions.NotExistException
import com.bd.examples.kafka.web.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@Component
@RestControllerAdvice
class HttpExceptionHandler {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(HttpExceptionHandler::class.java)
    }

    @ExceptionHandler(NotExistException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun notExistsExceptionHandler(ex: NotExistException) =
            ResponseDto(400, ex.message ?: "Requested object was not created")

    @ExceptionHandler(AlreadyExistsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun alreadyExistsExceptionHandler(ex: AlreadyExistsException) =
            ResponseDto(400, ex.message ?: "Object to create already exists")
}
