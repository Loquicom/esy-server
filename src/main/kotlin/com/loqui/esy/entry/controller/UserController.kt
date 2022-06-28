package com.loqui.esy.entry.controller

import com.loqui.esy.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@CrossOrigin
class UserController(
    @Autowired private val service: UserService
) {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello !!!"
    }

}
