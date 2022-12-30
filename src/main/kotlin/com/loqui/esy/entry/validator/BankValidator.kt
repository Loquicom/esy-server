package com.loqui.esy.entry.validator

import com.loqui.esy.data.request.BankRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BankValidator(
    @Autowired private val uuidValidator: UuidValidator
) : Validator<BankRequest>() {

    object BankValidatorConstant {
        const val NAME_REQUIRED = "Bank name is required"
        const val UUID_INVALID = "User id is invalid"
    }

    override fun validate(data: BankRequest) {
        assertThat(data.name).isNotBlank().orError(BankValidatorConstant.NAME_REQUIRED)
        assertThat(uuidValidator.isValid(data.user.toString())).isTrue().orError(BankValidatorConstant.UUID_INVALID)
    }

}
