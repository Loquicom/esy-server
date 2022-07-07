package com.loqui.esy.entry.controller

import com.loqui.esy.data.view.ErrorView
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.exception.EsyException
import com.loqui.esy.utils.error
import com.loqui.esy.utils.response
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionController(
    @Value("\${esy.security.hide-internal-error}") private val hideInternalErrorValue: String
) {

    private val hideInternalError: Boolean by lazy {
        hideInternalErrorValue.toBoolean()
    }

    @ExceptionHandler(EsyException::class)
    fun esyExceptionHandler(request: HttpServletRequest, exception: EsyException): ResponseEntity<ResponseView<ErrorView>> {
        return response(exception)
    }

    @ExceptionHandler(Exception::class)
    fun internalServerErrorHandler(request: HttpServletRequest, exception: Exception): ResponseEntity<ResponseView<ErrorView>> {
        return error(exception, this.hideInternalError)
    }

}
