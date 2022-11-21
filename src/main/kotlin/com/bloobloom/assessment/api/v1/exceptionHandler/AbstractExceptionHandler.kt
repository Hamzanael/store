package com.bloobloom.assessment.api.v1.exceptionHandler

import com.bloobloom.assessment.api.v1.response.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

abstract class AbstractExceptionHandler {

    protected fun prepareExceptionResponse(
        exceptionMessage: String,
        status: HttpStatus
    ): ResponseEntity<ExceptionResponse> {
        return ResponseEntity<ExceptionResponse>(
            ExceptionResponse(
                exceptionMessage,
                status
            ),
            status
        )
    }
}
