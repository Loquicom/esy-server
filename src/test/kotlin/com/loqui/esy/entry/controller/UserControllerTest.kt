package com.loqui.esy.entry.controller

import com.loqui.esy.maker.makeRegisterRequest
import com.loqui.esy.maker.makeWrappedLoginView
import com.loqui.esy.maker.toJSON
import com.loqui.esy.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTest : ControllerTest() {

    @MockBean
    private lateinit var service: UserService

    protected val path by lazy {
        "$rootPath/users"
    }

    @Test
    fun registerTest() {
        val request = makeRegisterRequest()
        val response = makeWrappedLoginView()
        Mockito.`when`(service.register(request)).thenReturn(response)
        mockMvc
            .perform(
                post("$path/register").contentType(MediaType.APPLICATION_JSON)
                    .content(toJSON(request))
            )
            .andExpect(status().isOk)
            .andExpect(content().json(toJSON(response)))
    }

}
