package com.loqui.esy.data.wrapper

data class ResultWrapper<T>(
    val data: T
) {

    val success: Boolean by lazy {
        this.data !is EsyError
    }

}
