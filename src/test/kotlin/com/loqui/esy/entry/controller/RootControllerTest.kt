package com.loqui.esy.entry.controller

import com.loqui.esy.maker.LOGIN
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class RootControllerTest : ControllerTest() {

    @Test
    fun infoTest() {
        mockMvc
            .perform(
                get("$rootPath/")
            )
            .andExpect(status().isOk)
    }

    @Test
    fun pingTest() {
        mockMvc
            .perform(
                get("$rootPath/ping")
            )
            .andExpect(status().isOk)
            .andExpect(content().string("pong"))
    }

    @Test
    fun sayTest() {
        val word = "test"
        mockMvc
            .perform(
                get("$rootPath/say/$word")
            )
            .andExpect(status().isOk)
            .andExpect(content().string("Esy server: $word"))
    }

    @Test
    @WithUserDetails(LOGIN)
    fun authSuccessTest() {
        mockMvc
            .perform(
                get("$rootPath/auth").headers(authHeader())
            )
            .andExpect(status().isOk)
            .andExpect(content().string("You are authentified"))
    }

    @Test
    fun authUnauthorizedTest() {
        mockMvc
            .perform(
                get("$rootPath/auth")
            )
            .andExpect(status().isUnauthorized)
    }

}
