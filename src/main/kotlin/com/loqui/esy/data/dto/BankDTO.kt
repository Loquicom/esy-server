package com.loqui.esy.data.dto

import java.util.*

data class BankDTO(
    val id: UUID,
    val name: String,
    val description: String,
    val main: Boolean
) {
}
