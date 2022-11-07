package com.loqui.esy.entry.validator

import com.loqui.esy.data.request.LoginRequest
import com.loqui.esy.maker.user.makeLoginRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class LoginValidatorTest : ValidatorTest() {

    @Autowired
    private lateinit var validator: LoginValidator

    @Test
    fun contextLoad() {
        assertThat(validator).isNotNull
    }

    @Test
    fun validTest() {
        val request = makeLoginRequest()
        assertThat(validator.isValid(request)).isTrue
    }

    @Test
    fun validListTest() {
        val request = ArrayList<LoginRequest>()
        for (i in 1..3) {
            request.add(makeLoginRequest(i))
        }
        assertThat(validator.isValid(request)).isTrue
    }

    @Test
    fun notValidTest() {
        val request = LoginRequest(
            "",
            ""
        )
        assertThat(validator.isValid(request)).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(2)
        assertThat(validator.getErrors().contains(LoginValidator.LoginValidatorConstant.LOGIN_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains(LoginValidator.LoginValidatorConstant.PASSWORD_REQUIRED)).isTrue
    }

    @Test
    fun notValidListTest() {
        val request = ArrayList<LoginRequest>()
        request.add(LoginRequest("", ""))
        request.add(LoginRequest("    ", "      "))
        assertThat(validator.isValid(request)).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(4)
        assertThat(validator.getErrors().contains("1: " + LoginValidator.LoginValidatorConstant.LOGIN_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains("1: " + LoginValidator.LoginValidatorConstant.PASSWORD_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains("2: " + LoginValidator.LoginValidatorConstant.LOGIN_REQUIRED)).isTrue
        assertThat(validator.getErrors().contains("2: " + LoginValidator.LoginValidatorConstant.PASSWORD_REQUIRED)).isTrue
    }

}
