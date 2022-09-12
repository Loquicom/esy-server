package com.loqui.esy.entry.validator

import org.springframework.stereotype.Component
import java.util.*

@Component
class UuidValidator : Validator<String>() {

    object UuidValidatorConstant {
        const val UUID_INVALID = "UUID is invalid"
    }

    override fun validate(data: String) {
        assertThatNotThrow({ UUID.fromString(data) }, UuidValidatorConstant.UUID_INVALID)
    }
}
