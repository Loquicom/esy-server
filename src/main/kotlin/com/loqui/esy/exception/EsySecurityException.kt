package com.loqui.esy.exception

import com.loqui.esy.data.wrapper.EsyError
import org.springframework.http.HttpStatus

open class EsySecurityException(
    message: String? = null,
    code: Int = 0,
    status: HttpStatus = HttpStatus.FORBIDDEN,
    throwable: Throwable? = null
) : EsyException(message, code, status, throwable) {

    constructor(error: EsyError, throwable: Throwable? = null) : this(error.message, error.code, error.status, throwable)

    constructor(message: String? = null, throwable: Throwable? = null) : this(message, 0, HttpStatus.FORBIDDEN, throwable)

    constructor(message: String? = null, status: HttpStatus, throwable: Throwable? = null) : this(message, 0, status, throwable)

}
