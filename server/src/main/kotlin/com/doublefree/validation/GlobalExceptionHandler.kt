package com.doublefree.validation

import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@RequiredArgsConstructor
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler
    fun handleException(exception: ValidationExceptionDto?, request: WebRequest?) =
        ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception)
}
