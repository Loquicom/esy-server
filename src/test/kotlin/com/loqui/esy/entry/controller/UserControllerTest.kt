package com.loqui.esy.entry.controller

import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.entry.validator.LoginValidator
import com.loqui.esy.entry.validator.RegisterValidator
import com.loqui.esy.exception.EsyAuthenticationException
import com.loqui.esy.exception.EsyValidatorException
import com.loqui.esy.maker.*
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

    @MockBean
    private lateinit var registerValidator: RegisterValidator

    @MockBean
    private lateinit var loginValidator: LoginValidator

    protected val path by lazy {
        "$rootPath/users"
    }

    @Test
    fun registerSuccessTest() {
        val request = makeRegisterRequest()
        val result = makeLoginView()
        val response = toResponse(result)
        Mockito.`when`(service.register(request)).thenReturn(result)
        mockMvc
            .perform(
                post("$path/register").contentType(MediaType.APPLICATION_JSON)
                    .content(toJSON(request))
            )
            .andExpect(status().isOk)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    fun registerFailTest() {
        val request = makeRegisterRequest()
        val result = EsyError.REGISTER_LOGIN_ALREADY_EXIST
        val response = toResponse(result)
        Mockito.`when`(service.register(request)).thenThrow(EsyAuthenticationException(result))
        mockMvc
            .perform(
                post("$path/register").contentType(MediaType.APPLICATION_JSON)
                    .content(toJSON(request))
            )
            .andExpect(status().isUnauthorized)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    fun registerBadRequestTest() {
        val request = makeRegisterRequest()
        val result = EsyError.BAD_REQUEST
        val response = toResponse(result)
        Mockito.`when`(registerValidator.isValidOrThrow(request)).thenThrow(EsyValidatorException(result))
        mockMvc
            .perform(
                post("$path/register").contentType(MediaType.APPLICATION_JSON)
                    .content(toJSON(request))
            )
            .andExpect(status().isBadRequest)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    fun loginSuccessTest() {
        val request = makeLoginRequest()
        val result = makeLoginView()
        val response = toResponse(result)
        Mockito.`when`(service.login(request)).thenReturn(result)
        mockMvc
            .perform(
                post("$path/login").contentType(MediaType.APPLICATION_JSON)
                    .content(toJSON(request))
            )
            .andExpect(status().isOk)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    fun loginFailTest() {
        val request = makeLoginRequest()
        val result = EsyError.AUTHENTICATION
        val response = toResponse(result)
        Mockito.`when`(service.login(request)).thenThrow(EsyAuthenticationException(result))
        mockMvc
            .perform(
                post("$path/login").contentType(MediaType.APPLICATION_JSON)
                    .content(toJSON(request))
            )
            .andExpect(status().isUnauthorized)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    fun loginBadRequestTest() {
        val request = makeLoginRequest()
        val result = EsyError.BAD_REQUEST
        val response = toResponse(result)
        Mockito.`when`(loginValidator.isValidOrThrow(request)).thenThrow(EsyValidatorException(result))
        mockMvc
            .perform(
                post("$path/login").contentType(MediaType.APPLICATION_JSON)
                    .content(toJSON(request))
            )
            .andExpect(status().isBadRequest)
            .andExpect(content().json(toJSON(response)))
    }

}
