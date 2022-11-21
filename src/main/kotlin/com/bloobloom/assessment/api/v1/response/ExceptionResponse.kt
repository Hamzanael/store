package com.bloobloom.assessment.api.v1.response

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.springframework.http.HttpStatus

data class ExceptionResponse(
    var errorMessage: String,
    val httpStatus: HttpStatus,
    val time: String = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z").format(ZonedDateTime.now())
)
