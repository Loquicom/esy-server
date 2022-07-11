package com.loqui.esy.entry.validator

import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class EmailValidator : Validator<String>() {

    override fun validate(data: String) {
        val pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$")
        assertThat(pattern.matcher(data).matches()).isTrue().valid()
    }
}
