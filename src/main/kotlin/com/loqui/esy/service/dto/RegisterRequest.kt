package com.loqui.esy.service.dto

data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String
)
