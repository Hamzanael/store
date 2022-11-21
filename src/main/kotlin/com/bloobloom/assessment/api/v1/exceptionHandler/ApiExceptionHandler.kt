package com.bloobloom.assessment.api.v1.exceptionHandler

import com.bloobloom.assessment.api.v1.response.ExceptionResponse
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler : AbstractExceptionHandler() {

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(UNAUTHORIZED)
    fun unauthorizedToAccessEndPoint(throwable: Throwable): ResponseEntity<ExceptionResponse> {
        return prepareExceptionResponse("UNAUTHORIZED", UNAUTHORIZED)
    }

    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        MissingKotlinParameterException::class,
        HttpMessageNotReadableException::class
    )
    @ResponseStatus(BAD_REQUEST)
    fun validationExceptions(throwable: Throwable): ResponseEntity<ExceptionResponse> {
        return prepareExceptionResponse(
            "EX.MISSING_PARAMETERS ${throwable.cause}",
            BAD_REQUEST
        )
    }
}
