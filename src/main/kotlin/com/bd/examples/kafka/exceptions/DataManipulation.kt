package com.bd.examples.kafka.exceptions

import java.lang.Exception

class AlreadyExistsException(message: String) : Exception(message)
class NotExistException(message: String) : Exception(message)
