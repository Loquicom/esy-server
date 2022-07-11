package com.loqui.esy.entry.validator

import com.loqui.esy.data.request.LoginRequest
import org.springframework.stereotype.Component

@Component
class LoginValidator : Validator<LoginRequest>() {

    object LoginValidatorConstant {
        const val LOGIN_REQUIRED = "Login is required"
        const val PASSWORD_REQUIRED = "Password is required"
    }

    override fun validate(data: LoginRequest) {
        assertThat(data.login).isNotBlank().orError(LoginValidatorConstant.LOGIN_REQUIRED)
        assertThat(data.password).isNotBlank().orError(LoginValidatorConstant.PASSWORD_REQUIRED)
    }
}
