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
import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<ResponseView<LoginView>> {
        log.info("Begin register, login={}", request.login)
        registerValidator.isValidOrThrow(request)
        val result = service.register(request)
        log.info("End register")
        return response(result)
    }

    @PostMapping("login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<ResponseView<LoginView>> {
        log.info("Begin login, login={}", request.login)
        loginValidator.isValidOrThrow(request)
        val result = service.login(request)
        log.info("End login")
        return response(result)
    }

    @GetMapping("token/{token}/validate")
    fun validate(@PathVariable token: String): ResponseEntity<SuccessView> {
        log.info("Begin validate, token={}", token)
        val result = service.validate(token)
        log.info("End validate")
        return response(result, HttpStatus.OK)
    }

    @PutMapping("token/refresh")
    fun refresh(@RequestHeader("Authorization") authorization: String): ResponseEntity<ResponseView<LoginView>> {
        log.info("Begin refresh")
        if (authorization.length <= 7 || authorization.substring(0, 7) != "Bearer ") {
            log.info("Invalid Authorization header")
            throw EsyValidatorException(EsyError.INVALID_AUTHORIZATION)
        }
        val token = authorization.substring(7)
        log.info("End refresh")
        return response(service.refresh(token))
    }

}
