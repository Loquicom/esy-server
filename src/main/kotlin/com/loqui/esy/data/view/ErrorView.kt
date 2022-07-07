package com.loqui.esy.data.view

import java.util.*

data class ErrorView(
    val code: Int,
    val message: String,
    val trace: List<String> = Collections.emptyList()
)
