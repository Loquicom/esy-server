package com.loqui.esy.entry.controller

import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.entry.validator.LoginValidator
import com.loqui.esy.entry.validator.RegisterValidator
import com.loqui.esy.entry.validator.UuidValidator
import com.loqui.esy.exception.EsyAuthenticationException
import com.loqui.esy.exception.EsyNotFoundException
import com.loqui.esy.exception.EsyValidatorException
import com.loqui.esy.maker.failResponse
import com.loqui.esy.maker.successResponse
import com.loqui.esy.maker.toResponse
import com.loqui.esy.maker.toUUID
import com.loqui.esy.maker.user.*
import com.loqui.esy.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.mock.mockito.MockBean

class UserControllerTest : ControllerTest() {

    @MockBean
    private lateinit var service: UserService

    @MockBean
    private lateinit var registerValidator: RegisterValidator

    @MockBean
    private lateinit var loginValidator: LoginValidator

    @MockBean
    private lateinit var uuidValidator: UuidValidator

    override val path = "users"

    @Test
    fun registerSuccessTest() {
        val request = makeRegisterRequest()
        val result = makeLoginView()
        val response = toResponse(result)

        Mockito.`when`(service.register(request)).thenReturn(result)

        post("register")
            .content(request)
            .status(ResultHttpStatus.OK)
            .expect(response)
    }

    @Test
    fun registerFailTest() {
        val request = makeRegisterRequest()
        val result = EsyError.REGISTER_LOGIN_ALREADY_EXIST
        val response = toResponse(result)

        Mockito.`when`(service.register(request)).thenThrow(EsyAuthenticationException(result))

        post("register")
            .content(request)
            .status(ResultHttpStatus.UNAUTHORIZED)
            .expect(response)
    }

    @Test
    fun registerBadRequestTest() {
        val request = makeRegisterRequest()
        val result = EsyError.BAD_REQUEST
        val response = toResponse(result)

        Mockito.`when`(registerValidator.isValidOrThrow(request)).thenThrow(EsyValidatorException(result))

        post("register")
            .content(request)
            .status(ResultHttpStatus.BAD_REQUEST)
            .expect(response)
    }

    @Test
    fun loginSuccessTest() {
        val request = makeLoginRequest()
        val result = makeLoginView()
        val response = toResponse(result)

        Mockito.`when`(service.login(request)).thenReturn(result)

        post("login")
            .content(request)
            .status(ResultHttpStatus.OK)
            .expect(response)
    }

    @Test
    fun loginFailTest() {
        val request = makeLoginRequest()
        val result = EsyError.AUTHENTICATION
        val response = toResponse(result)

        Mockito.`when`(service.login(request)).thenThrow(EsyAuthenticationException(result))

        post("login")
            .content(request)
            .status(ResultHttpStatus.UNAUTHORIZED)
            .expect(response)
    }

    @Test
    fun loginBadRequestTest() {
        val request = makeLoginRequest()
        val result = EsyError.BAD_REQUEST
        val response = toResponse(result)

        Mockito.`when`(loginValidator.isValidOrThrow(request)).thenThrow(EsyValidatorException(result))

        post("login")
            .content(request)
            .status(ResultHttpStatus.BAD_REQUEST)
            .expect(response)
    }

    @Test
    fun validateSuccessTest() {
        val response = successResponse()

        Mockito.`when`(service.validate(JWT_TOKEN)).thenReturn(true)

        get("token/$JWT_TOKEN/validate")
            .status(ResultHttpStatus.OK)
            .expect(response)
    }

    @Test
    fun validateFailTest() {
        val response = failResponse()

        Mockito.`when`(service.validate(JWT_TOKEN)).thenReturn(false)

        get("token/$JWT_TOKEN/validate")
            .status(ResultHttpStatus.OK)
            .expect(response)
    }

    @Test
    fun refreshSuccessTest() {
        val result = makeLoginView()
        val response = toResponse(result)

        Mockito.`when`(service.refresh(JWT_TOKEN)).thenReturn(result)

        put("token/refresh")
            .authHeader()
            .status(ResultHttpStatus.OK)
            .expect(response)
    }

    @Test
    fun refreshInvalidAuthorization1Test() {
        val result = makeLoginView()

        Mockito.`when`(service.refresh(JWT_TOKEN)).thenReturn(result)

        put("token/refresh")
            .header("Authorization", "NotBearer $JWT_TOKEN")
            .status(ResultHttpStatus.BAD_REQUEST)
    }

    @Test
    fun refreshInvalidAuthorization2Test() {
        val result = makeLoginView()

        Mockito.`when`(service.refresh(JWT_TOKEN)).thenReturn(result)

        put("token/refresh")
            .header("Authorization", "")
            .status(ResultHttpStatus.BAD_REQUEST)
    }

    @Test
    fun refreshUnauthorizedTest() {
        put("refresh")
            .status(ResultHttpStatus.UNAUTHORIZED)
    }

    @Test
    fun getSuccessTest() {
        val user = makeUser(ID)
        val expected = makeUserDTO(ID)
        val response = toResponse(expected)

        Mockito.`when`(service.get(toUUID(ID))).thenReturn(user)

        get(ID)
            .authHeader()
            .status(ResultHttpStatus.OK)
            .expect(response)
    }

    @Test
    fun getBadRequestTest() {
        val expected = EsyError.BAD_REQUEST
        val response = toResponse(expected)

        Mockito.`when`(uuidValidator.isValidOrThrow(ID)).thenThrow(EsyValidatorException(expected))

        get(ID)
            .authHeader()
            .status(ResultHttpStatus.BAD_REQUEST)
            .expect(response)
    }

    @Test
    fun getNotFoundTest() {
        Mockito.`when`(service.get(toUUID(ID))).thenThrow(EsyNotFoundException())

        get(ID)
            .authHeader()
            .status(ResultHttpStatus.BAD_REQUEST)
    }

    @Test
    fun getUnauthorizedTest() {
        get(ID)
            .status(ResultHttpStatus.UNAUTHORIZED)
    }

}
