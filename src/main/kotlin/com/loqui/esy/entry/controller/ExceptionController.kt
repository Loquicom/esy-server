package com.loqui.esy.entry.controller

import com.loqui.esy.data.view.ErrorView
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.exception.EsyException
import com.loqui.esy.utils.error
import com.loqui.esy.utils.response
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionController(
    @Value("\${esy.security.hide-internal-error}") private val hideInternalError: Boolean
) {

    private val log = LoggerFactory.getLogger(ExceptionController::class.java)
    
    @ExceptionHandler(EsyException::class)
    fun esyExceptionHandler(request: HttpServletRequest, exception: EsyException): ResponseEntity<ResponseView<ErrorView>> {
        log.info("Esy exception,", exception)
        log.trace(exception.stackTraceToString())
        return response(exception)
    }

    @ExceptionHandler(Exception::class)
    fun internalServerErrorHandler(request: HttpServletRequest, exception: Exception): ResponseEntity<ResponseView<ErrorView>> {
        log.warn("Unattended exception,", exception)
        log.trace(exception.stackTraceToString())
        return error(exception, this.hideInternalError)
    }

}
