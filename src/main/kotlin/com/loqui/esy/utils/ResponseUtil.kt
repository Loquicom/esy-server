package com.loqui.esy.utils

import com.loqui.esy.data.view.ErrorView
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.data.wrapper.ResultWrapper
import org.springframework.http.ResponseEntity

fun <T> response(result: ResultWrapper<T>): ResponseEntity<ResponseView<*>> {
    return if (result.success) {
        success(result) as ResponseEntity<ResponseView<*>>
    } else {
        error(result as ResultWrapper<EsyError>) as ResponseEntity<ResponseView<*>>
    }
}

fun <T> success(result: ResultWrapper<T>): ResponseEntity<ResponseView<T>> {
    val response = ResponseView(
        true,
        result.data
    )
    return ResponseEntity.ok()
        .body(response)
}

fun error(result: ResultWrapper<EsyError>): ResponseEntity<ResponseView<ErrorView>> {
    val response = ResponseView(
        false,
        ErrorView(result.data.code, result.data.message)
    )
    return ResponseEntity.status(result.data.status)
        .body(response)
}
