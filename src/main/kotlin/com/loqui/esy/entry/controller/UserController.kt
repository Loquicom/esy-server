package com.loqui.esy.entry.controller

import com.loqui.esy.data.request.LoginRequest
import com.loqui.esy.data.request.RegisterRequest
import com.loqui.esy.data.view.LoginView
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.data.view.SuccessView
import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.entry.validator.LoginValidator
import com.loqui.esy.entry.validator.RegisterValidator
import com.loqui.esy.exception.EsyValidatorException
import com.loqui.esy.service.UserService
import com.loqui.esy.utils.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("\${esy.api.root-path}/users")
class UserController(
    @Autowired private val service: UserService,
    @Autowired private val registerValidator: RegisterValidator,
    @Autowired private val loginValidator: LoginValidator
) {

    @PostMapping("register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<ResponseView<LoginView>> {
        registerValidator.isValidOrThrow(request)
        return response(service.register(request))
    }

    @PostMapping("login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<ResponseView<LoginView>> {
        loginValidator.isValidOrThrow(request)
        return response(service.login(request))
    }

    @GetMapping("token/{token}/validate")
    fun validate(@PathVariable token: String): ResponseEntity<SuccessView> {
        return response(service.validate(token), HttpStatus.OK)
    }

    @PutMapping("token/refresh")
    fun refresh(@RequestHeader("Authorization") authorization: String): ResponseEntity<ResponseView<LoginView>> {
        if (authorization.length <= 7 || authorization.substring(0, 7) != "Bearer ") {
            throw EsyValidatorException(EsyError.INVALID_AUTHORIZATION)
        }
        val token = authorization.substring(7)
        return response(service.refresh(token))
    }

}
