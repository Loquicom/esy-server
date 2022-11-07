package com.loqui.esy.entry.controller

import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.entry.validator.LoginValidator
import com.loqui.esy.entry.validator.RegisterValidator
import com.loqui.esy.entry.validator.UuidValidator
import com.loqui.esy.exception.EsyAuthenticationException
import com.loqui.esy.exception.EsyNotFoundException
import com.loqui.esy.exception.EsyValidatorException
import com.loqui.esy.maker.*
import com.loqui.esy.maker.user.*
import com.loqui.esy.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTest : ControllerTest() {

    @MockBean
    private lateinit var service: UserService

    @MockBean
    private lateinit var registerValidator: RegisterValidator

    @MockBean
    private lateinit var loginValidator: LoginValidator

    @MockBean
    private lateinit var uuidValidator: UuidValidator

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

    @Test
    fun validateSuccessTest() {
        val response = successResponse()
        Mockito.`when`(service.validate(JWT_TOKEN)).thenReturn(true)
        mockMvc
            .perform(get("$path/token/$JWT_TOKEN/validate"))
            .andExpect(status().isOk)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    fun validateFailTest() {
        val response = failResponse()
        Mockito.`when`(service.validate(JWT_TOKEN)).thenReturn(false)
        mockMvc
            .perform(get("$path/token/$JWT_TOKEN/validate"))
            .andExpect(status().isOk)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    @WithUserDetails(LOGIN)
    fun refreshSuccessTest() {
        val result = makeLoginView()
        val response = toResponse(result)
        Mockito.`when`(service.refresh(JWT_TOKEN)).thenReturn(result)
        mockMvc
            .perform(
                put("$path/token/refresh")
                    .headers(authHeader())
            )
            .andExpect(status().isOk)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    @WithUserDetails(LOGIN)
    fun refreshInvalidAuthorization1Test() {
        val result = makeLoginView()
        Mockito.`when`(service.refresh(JWT_TOKEN)).thenReturn(result)
        mockMvc
            .perform(
                put("$path/token/refresh")
                    .header("Authorization", "NotBearer $JWT_TOKEN")
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithUserDetails(LOGIN)
    fun refreshInvalidAuthorization2Test() {
        val result = makeLoginView()
        Mockito.`when`(service.refresh(JWT_TOKEN)).thenReturn(result)
        mockMvc
            .perform(
                put("$path/token/refresh")
                    .header("Authorization", "")
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun refreshUnauthorizedTest() {
        mockMvc
            .perform(put("$path/refresh"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun getSuccessTest() {
        val user = makeUser(ID)
        val expected = makeUserDTO(ID)
        val response = toResponse(expected)

        Mockito.`when`(service.get(toUUID(ID))).thenReturn(user)

        mockMvc.perform(get("$path/$ID").headers(authHeader()))
            .andExpect(status().isOk)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    fun getBadRequestTest() {
        val expected = EsyError.BAD_REQUEST
        val response = toResponse(expected)

        Mockito.`when`(uuidValidator.isValidOrThrow(ID)).thenThrow(EsyValidatorException(expected))

        mockMvc.perform(get("$path/$ID").headers(authHeader()))
            .andExpect(status().isBadRequest)
            .andExpect(content().json(toJSON(response)))
    }

    @Test
    fun getNotFoundTest() {
        Mockito.`when`(service.get(toUUID(ID))).thenThrow(EsyNotFoundException())

        mockMvc.perform(get("$path/$ID").headers(authHeader()))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun getUnauthorizedTest() {
        mockMvc.perform(get("$path/$ID"))
            .andExpect(status().isUnauthorized)
    }

}
