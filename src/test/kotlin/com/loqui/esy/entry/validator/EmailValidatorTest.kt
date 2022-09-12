package com.loqui.esy.entry.validator

import com.loqui.esy.maker.EMAIL
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class EmailValidatorTest : ValidatorTest() {

    @Autowired
    private lateinit var validator: EmailValidator

    @Test
    fun contextLoad() {
        assertThat(validator).isNotNull
    }

    @Test
    fun validTest() {
        assertThat(validator.isValid(EMAIL)).isTrue
        assertThat(validator.isValid("1-$EMAIL")).isTrue
    }

    @Test
    fun validListTest() {
        val list = ArrayList<String>()
        list.add(EMAIL)
        list.add("1-$EMAIL")
        assertThat(validator.isValid(list)).isTrue
    }

    @Test
    fun notValidTest() {
        assertThat(validator.isValid("is not an email")).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(1)
        assertThat(validator.getError(0)).isEqualTo(EmailValidator.EmailValidatorConstant.EMAIL_INVALID)
        assertThat(validator.isValid("invalid.email.com")).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(1)
        assertThat(validator.getError(0)).isEqualTo(EmailValidator.EmailValidatorConstant.EMAIL_INVALID)
        assertThat(validator.isValid("is an invalid@email.com")).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(1)
        assertThat(validator.getError(0)).isEqualTo(EmailValidator.EmailValidatorConstant.EMAIL_INVALID)
        assertThat(validator.isValid("invalid@email.c")).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(1)
        assertThat(validator.getError(0)).isEqualTo(EmailValidator.EmailValidatorConstant.EMAIL_INVALID)
        assertThat(validator.isValid("invalid@email.company")).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(1)
        assertThat(validator.getError(0)).isEqualTo(EmailValidator.EmailValidatorConstant.EMAIL_INVALID)
        assertThat(validator.isValid("invalid@.email.com")).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(1)
        assertThat(validator.getError(0)).isEqualTo(EmailValidator.EmailValidatorConstant.EMAIL_INVALID)
    }

    @Test
    fun notValidListTest() {
        val list = ArrayList<String>()
        list.add(EMAIL)
        list.add("invalid@email.company")
        list.add("1-$EMAIL")
        list.add("invalid.email.com")
        assertThat(validator.isValid(list)).isFalse
        assertThat(validator.getNumberOfErrors()).isEqualTo(2)
        assertThat(validator.getError(0)).isEqualTo("2: " + EmailValidator.EmailValidatorConstant.EMAIL_INVALID)
        assertThat(validator.getError(1)).isEqualTo("4: " + EmailValidator.EmailValidatorConstant.EMAIL_INVALID)
    }
}
