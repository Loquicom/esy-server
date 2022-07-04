package com.loqui.esy.maker

import com.fasterxml.jackson.databind.ObjectMapper
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

fun <T> toJSON(obj: T): String {
    return objectMapper.writeValueAsString(obj)
}
