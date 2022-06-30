package com.loqui.esy.entry.controller

import com.loqui.esy.service.UserService
import com.loqui.esy.service.dto.LoginRequest
import com.loqui.esy.service.dto.LoginView
import com.loqui.esy.service.dto.RegisterRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/users")
class UserController(
    @Autowired private val service: UserService
) {

    @PostMapping("register")
    fun register(@RequestBody request: RegisterRequest): LoginView {
        return service.register(request)
    }

    @PostMapping("login")
    fun login(@RequestBody request: LoginRequest): LoginView {
        return service.login(request)
    }

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello !!!"
    }

}
