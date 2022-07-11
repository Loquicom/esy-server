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
    }

    @Test
    fun notValidTest() {
        assertThat(validator.isValid("is not an email")).isFalse
        assertThat(validator.isValid("invalid.email.com")).isFalse
        assertThat(validator.isValid("is an invalid@email.com")).isFalse
        assertThat(validator.isValid("invalid@email.c")).isFalse
        assertThat(validator.isValid("invalid@email.company")).isFalse
        assertThat(validator.isValid("invalid@.email.com")).isFalse
    }
}
