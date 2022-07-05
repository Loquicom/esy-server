package com.loqui.esy.data.wrapper

data class ResultWrapper<T>(
    val data: T
) {

    val isSuccess: Boolean by lazy {
        this.data !is EsyError
    }

    val isError: Boolean by lazy {
        this.data is EsyError
    }

}
