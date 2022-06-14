package com.loqui.esy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EsyApplication

fun main(args: Array<String>) {
	runApplication<EsyApplication>(*args)
}
