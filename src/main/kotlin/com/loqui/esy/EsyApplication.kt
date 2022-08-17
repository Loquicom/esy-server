package com.loqui.esy

import com.loqui.esy.utils.JWTUtil
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.security.InvalidParameterException

@EnableScheduling
@SpringBootApplication
class EsyApplication

fun main(args: Array<String>) {
    when (args.count()) {
        0 -> runApplication<EsyApplication>(*args)
        1 -> if (args[0] == "generate-key") println("Key ${JWTUtil().getKey()}") else throw InvalidParameterException("Invalid argument: ${args[0]}")
        else -> throw InvalidParameterException("Invalid number of argument: ${args.count()}")
    }
}
