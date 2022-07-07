package com.loqui.esy.utils

import com.loqui.esy.data.view.ErrorView
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.exception.EsyException
import org.springframework.http.ResponseEntity

fun response(result: EsyException): ResponseEntity<ResponseView<ErrorView>> {
    val response = ResponseView(
        false,
        result.toErrorView()
    )
    return ResponseEntity.status(result.status).body(response)
}

fun <T> response(result: T): ResponseEntity<ResponseView<T>> {
    val response = ResponseView(
        true,
        result
    )
    return ResponseEntity.ok().body(response)
}

fun error(exception: Exception, hide: Boolean = false): ResponseEntity<ResponseView<ErrorView>> {
    val message = exception.message
    return if (!hide && message != null) {
        response(EsyException(message, exception))
    } else {
        response(EsyException(EsyError.UNKNOWN))
    }
}
