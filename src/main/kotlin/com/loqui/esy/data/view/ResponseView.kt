package com.loqui.esy.data.view

data class ResponseView<T>(
    val success: Boolean,
    val data: T
)
