package com.loqui.esy.entry.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@CrossOrigin
class UserController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello !!!";
    }

}
