package com.loqui.esy.service.dto

import java.util.*

data class UserDTO(
    val id: UUID,
    val login: String,
    val email: String
)
