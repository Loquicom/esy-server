package com.loqui.esy.data.dto

import java.util.*

data class UserDTO(
    val id: UUID,
    val login: String,
    val email: String,
    val role: List<String>
)
