package com.loqui.esy.entry.validator

import com.loqui.esy.data.request.RegisterRequest
import com.loqui.esy.maker.user.makeRegisterRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean

class RegisterValidatorTest : ValidatorTest() {

    @Autowired
    private lateinit var validator: RegisterValidator

    @MockBean
    private lateinit var emailValidator: EmailValidator

    @Test
    fun contextLoad() {
        assertThat(validator).isNotNull
    }

    @Test
    fun validTest() {
        val request = makeRegisterRequest()
        Mockito.`when`(emailValidator.isValid(request.email)).thenReturn(true)
        assertThat(validator.isValid(request)).isTrue
    }

    @Test
    fun validListTest() {
        val request = ArrayList<RegisterRequest>()
        for (i in 1..3) {
            request.add(makeRegisterRequest(i))
        }
        Mockito.`when`(emailValidator.isValid(Mockito.anyString())).thenReturn(true)
        assertThat(validator.isValid(request)).isTrue
    }

    @Test
    fun notValidTest() {
        val request = RegisterRequest(
            "",
            "",
            ""
        )
        Mockito.`when`(emailValidator.isValid(request.email)).thenReturn(false)

        assertThat(validator.isValid(request)).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(4)
        assertThat(validator.getErrors().contains(RegisterValidator.RegisterValidatorConstant.EMAIL_INVALID)).isTrue
        assertThat(validator.getErrors().contains(RegisterValidator.RegisterValidatorConstant.EMAIL_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains(RegisterValidator.RegisterValidatorConstant.LOGIN_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains(RegisterValidator.RegisterValidatorConstant.PASSWORD_REQUIRED)).isTrue
    }

    @Test
    fun notValidListTest() {
        val request = ArrayList<RegisterRequest>()
        request.add(RegisterRequest("", "", ""))
        request.add(RegisterRequest("     ", "      ", "test@.com"))
        Mockito.`when`(emailValidator.isValid(Mockito.anyString())).thenReturn(false)

        assertThat(validator.isValid(request)).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(7)
        assertThat(validator.getErrors().contains("1: " + RegisterValidator.RegisterValidatorConstant.EMAIL_INVALID)).isTrue
        assertThat(validator.getErrors().contains("1: " + RegisterValidator.RegisterValidatorConstant.EMAIL_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains("1: " + RegisterValidator.RegisterValidatorConstant.LOGIN_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains("1: " + RegisterValidator.RegisterValidatorConstant.PASSWORD_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains("2: " + RegisterValidator.RegisterValidatorConstant.EMAIL_INVALID)).isTrue
        assertThat(validator.getErrors().contains("2: " + RegisterValidator.RegisterValidatorConstant.LOGIN_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains("2: " + RegisterValidator.RegisterValidatorConstant.PASSWORD_REQUIRED)).isTrue
    }

}
