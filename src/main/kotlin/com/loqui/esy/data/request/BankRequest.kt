package com.loqui.esy.data.request

import java.util.*

data class BankRequest(
    val name: String,
    val description: String,
    val main: Boolean,
    val user: UUID
) {
}
