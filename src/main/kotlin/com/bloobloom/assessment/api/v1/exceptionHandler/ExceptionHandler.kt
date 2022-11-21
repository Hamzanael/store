package com.bloobloom.assessment.api.v1.exceptionHandler

import com.bloobloom.assessment.api.v1.response.ExceptionResponse
import com.bloobloom.assessment.service.exceptions.NoEntityFoundException
import com.bloobloom.assessment.service.exceptions.ServiceException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler : AbstractExceptionHandler() {

    @ExceptionHandler(ServiceException::class)
    fun entityException(entityException: Throwable): ResponseEntity<ExceptionResponse> {
        return if (entityException is NoEntityFoundException) {
            prepareExceptionResponse(entityException.message.toString(), NOT_FOUND)
        } else {
            prepareExceptionResponse(entityException.message.toString(), BAD_REQUEST)
        }
    }
}
