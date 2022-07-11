package com.loqui.esy.entry.validator

import com.loqui.esy.data.request.RegisterRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RegisterValidator(
    @Autowired private val emailValidator: EmailValidator
) : Validator<RegisterRequest>() {

    object RegisterValidatorConstant {
        const val EMAIL_REQUIRED = "Email is required"
        const val EMAIL_INVALID = "Email is invalid"
        const val LOGIN_REQUIRED = "Login is required"
        const val PASSWORD_REQUIRED = "Password is required"
    }

    override fun validate(data: RegisterRequest) {
        assertThat(data.email).isNotBlank().orError(RegisterValidatorConstant.EMAIL_REQUIRED)
        assertThat(emailValidator.isValid(data.email)).isTrue().orError(RegisterValidatorConstant.EMAIL_INVALID)
        assertThat(data.login).isNotBlank().orError(RegisterValidatorConstant.LOGIN_REQUIRED)
        assertThat(data.password).isNotBlank().orError(RegisterValidatorConstant.PASSWORD_REQUIRED)
    }
}
