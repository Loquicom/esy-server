package com.loqui.esy.exception

import com.loqui.esy.data.view.ErrorView
import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.utils.DEFAULT_EXCEPTION_MESSAGE
import org.springframework.http.HttpStatus

open class EsyException(
    override val message: String? = null,
    val code: Int = 0,
    val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val throwable: Throwable? = null
) : Exception(message, throwable) {

    private val trace: MutableList<String> = ArrayList()

    constructor(error: EsyError, throwable: Throwable? = null) : this(error.message, error.code, error.status, throwable)

    constructor(message: String? = null, throwable: Throwable? = null) : this(message, 0, HttpStatus.INTERNAL_SERVER_ERROR, throwable)

    fun trace(trace: String) {
        this.trace.add(trace)
    }

    fun trace(): String {
        return this.trace.joinToString { "\n" }
    }

    fun getTrace(): List<String> {
        return this.trace
    }

    fun toErrorView(): ErrorView {
        val message = this.message ?: DEFAULT_EXCEPTION_MESSAGE
        return ErrorView(
            this.code,
            message,
            this.getTrace()
        )
    }

}
