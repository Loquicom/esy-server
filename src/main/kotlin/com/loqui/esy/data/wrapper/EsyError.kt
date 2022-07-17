package com.loqui.esy.data.wrapper

import org.springframework.http.HttpStatus

enum class EsyError(
    val code: Int,
    val message: String,
    val status: HttpStatus
) {
    UNKNOWN(0, "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR),
    REGISTER_LOGIN_ALREADY_EXIST(1, "Login already exist", HttpStatus.UNAUTHORIZED),
    AUTHENTICATION(2, "Unable to authenticate user", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST(3, "Bad request", HttpStatus.BAD_REQUEST),
    INVALID_AUTHORIZATION(4, "Invalid Authorization header", HttpStatus.BAD_REQUEST)
}
