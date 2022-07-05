package com.loqui.esy.maker

import com.fasterxml.jackson.databind.ObjectMapper
import com.loqui.esy.data.view.ErrorView
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.data.wrapper.ResultWrapper

val objectMapper = ObjectMapper()

fun getPrefix(i: Int?): String {
    if (i != null) {
        return "-$i"
    }
    return ""
}

fun <T> wrapper(obj: T): ResultWrapper<T> {
    return ResultWrapper(obj)
}


fun <T> toResponse(obj: ResultWrapper<T>): ResponseView<*> {
    return if (obj.isSuccess) {
        toSuccessResponse(obj)
    } else {
        toErrorResponse(obj as ResultWrapper<EsyError>)
    }
}

fun <T> toSuccessResponse(obj: ResultWrapper<T>): ResponseView<T> {
    return ResponseView(
        true,
        obj.data
    )
}

fun toErrorResponse(error: ResultWrapper<EsyError>): ResponseView<ErrorView> {
    return ResponseView(
        false,
        ErrorView(error.data.code, error.data.message)
    )
}

fun <T> toJSON(obj: T): String {
    return objectMapper.writeValueAsString(obj)
}
