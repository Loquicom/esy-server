package com.loqui.esy.maker

import com.fasterxml.jackson.databind.ObjectMapper
import com.loqui.esy.data.view.ErrorView
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.data.wrapper.EsyError

val objectMapper = ObjectMapper()

fun getPrefix(i: Int?): String {
    if (i != null) {
        return "$i-"
    }
    return ""
}

fun <T> toResponse(data: T): ResponseView<T> {
    return ResponseView(
        true,
        data
    )
}

fun toResponse(error: EsyError): ResponseView<ErrorView> {
    return ResponseView(
        false,
        ErrorView(error.code, error.message)
    )
}


fun <T> toJSON(obj: T): String {
    return objectMapper.writeValueAsString(obj)
}
