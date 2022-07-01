package com.loqui.esy.data.request

data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String
)
