package com.loqui.esy.entry.controller

import com.loqui.esy.maker.user.LOGIN
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithUserDetails

class RootControllerTest : ControllerTest() {

    override val path = ""

    @Test
    fun infoTest() {
        get().status(ResultHttpStatus.OK)
    }

    @Test
    fun pingTest() {
        get("ping")
            .status(ResultHttpStatus.OK)
            .expectString("pong")
    }

    @Test
    fun sayTest() {
        val word = "test"

        get("say/$word")
            .status(ResultHttpStatus.OK)
            .expectString("Esy server: $word")
    }

    @Test
    @WithUserDetails(LOGIN)
    fun authSuccessTest() {
        get("auth")
            .authHeader()
            .status(ResultHttpStatus.OK)
            .expectString("You are authentified")
    }

    @Test
    fun authUnauthorizedTest() {
        get("auth")
            .status(ResultHttpStatus.UNAUTHORIZED)
    }

}
