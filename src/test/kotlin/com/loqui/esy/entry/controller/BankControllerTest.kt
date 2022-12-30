package com.loqui.esy.entry.controller

import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.entry.validator.BankValidator
import com.loqui.esy.entry.validator.UuidValidator
import com.loqui.esy.exception.EsyNotFoundException
import com.loqui.esy.exception.EsyValidatorException
import com.loqui.esy.maker.bank.*
import com.loqui.esy.maker.toResponse
import com.loqui.esy.maker.toUUID
import com.loqui.esy.maker.user.LOGIN
import com.loqui.esy.service.BankService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithUserDetails

class BankControllerTest : ControllerTest() {

    @MockBean
    private lateinit var bankService: BankService

    @MockBean
    private lateinit var uuidValidator: UuidValidator

    @MockBean
    private lateinit var bankValidator: BankValidator

    override val path: String = "banks"

    @Test
    @WithUserDetails(LOGIN)
    fun getSuccessTest() {
        val bank = makeBank()
        val expected = makeBankDTO()
        val response = toResponse(expected)

        Mockito.`when`(bankService.get(toUUID(ID))).thenReturn(bank)

        get(ID)
            .status(ResultHttpStatus.OK)
            .expect(response)
    }

    @Test
    fun getUnauthorizedTest() {
        get(ID)
            .status(ResultHttpStatus.UNAUTHORIZED)
    }

    @Test
    fun getInvalidIdTest() {
        val result = EsyError.BAD_REQUEST
        val response = toResponse(result)

        Mockito.`when`(uuidValidator.isValidOrThrow(ID)).thenThrow(EsyValidatorException(result))

        get(ID)
            .authHeader()
            .status(result.status)
            .expect(response)
    }

    @Test
    fun getUnknowIdTest() {
        Mockito.`when`(bankService.get(toUUID(ID))).thenThrow(EsyNotFoundException(ERROR))

        get(ID)
            .authHeader()
            .status(ResultHttpStatus.BAD_REQUEST)
    }

    @Test
    fun addSuccessTest() {
        val request = makeBankRequest()
        val bank = makeBank()
        val expected = makeBankDTO()
        val response = toResponse(expected)

        Mockito.`when`(bankService.add(request)).thenReturn(bank)

        post()
            .authHeader()
            .content(request)
            .status(ResultHttpStatus.OK)
            .expect(response)
    }

    @Test
    fun addUnauthorizedTest() {
        val request = makeBankRequest()

        post()
            .content(request)
            .status(ResultHttpStatus.UNAUTHORIZED)
    }

    @Test
    fun addInvalidRequestTest() {
        val request = makeBankRequest()
        val result = EsyError.BAD_REQUEST
        val response = toResponse(result)

        Mockito.`when`(bankValidator.isValidOrThrow(request)).thenThrow(EsyValidatorException(result))

        post()
            .authHeader()
            .content(request)
            .status(result.status)
            .expect(response)
    }

    @Test
    fun updateSuccessTest() {
        val request = makeBankRequest()
        val bank = makeBank()
        val expected = makeBankDTO()
        val response = toResponse(expected)

        Mockito.`when`(bankService.update(toUUID(ID), request)).thenReturn(bank)

        put(ID)
            .authHeader()
            .content(request)
            .status(ResultHttpStatus.OK)
            .expect(response)
    }

    @Test
    fun updateUnauthorizedTest() {
        val request = makeBankRequest()

        put(ID)
            .content(request)
            .status(ResultHttpStatus.UNAUTHORIZED)
    }

    @Test
    fun updateInvalidIdTest() {
        val request = makeBankRequest()
        val result = EsyError.BAD_REQUEST
        val response = toResponse(result)

        Mockito.`when`(uuidValidator.isValidOrThrow(ID)).thenThrow(EsyValidatorException(result))

        put(ID)
            .authHeader()
            .content(request)
            .status(result.status)
            .expect(response)
    }

    @Test
    fun updateInvalidRequestTest() {
        val request = makeBankRequest()
        val result = EsyError.BAD_REQUEST
        val response = toResponse(result)

        Mockito.`when`(bankValidator.isValidOrThrow(request)).thenThrow(EsyValidatorException(result))

        put(ID)
            .authHeader()
            .content(request)
            .status(result.status)
            .expect(response)
    }

    @Test
    fun updateUnknowIdTest() {
        val request = makeBankRequest()

        Mockito.`when`(bankService.update(toUUID(ID), request)).thenThrow(EsyNotFoundException(ERROR))

        put(ID)
            .authHeader()
            .content(request)
            .status(ResultHttpStatus.BAD_REQUEST)
    }

}
