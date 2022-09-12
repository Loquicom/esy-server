package com.loqui.esy.entry.validator

import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class EmailValidator : Validator<String>() {

    object EmailValidatorConstant {
        const val EMAIL_INVALID = "Email is not valid"
    }

    override fun validate(data: String) {
        val pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$")
        assertThat(pattern.matcher(data).matches()).isTrue().orError(EmailValidatorConstant.EMAIL_INVALID)
    }
}
