package com.loqui.esy.entry.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("\${esy.api.root-path}")
@CrossOrigin
class RootController {

    private val log = LoggerFactory.getLogger(RootController::class.java)

    @Operation(summary = "Display server info")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Server info",
                content = [(Content(mediaType = MediaType.TEXT_PLAIN_VALUE))]
            )
        ]
    )
    @GetMapping("/")
    fun info(): String {
        log.info("Call info")
        return """
            It's works
            
            Esy server by Loquicom
            Git repository: https://github.com/Loquicom/esy-server
            
            Open API: /openapi
            Documentation: /openapi/swagger
        """.trimIndent()
    }

    @Operation(summary = "Ping the server")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Pong",
                content = [(Content(mediaType = MediaType.TEXT_PLAIN_VALUE))]
            )
        ]
    )
    @GetMapping("/ping")
    fun ping(): String {
        log.info("Call ping")
        return "pong"
    }

    @Operation(summary = "Say one word")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "The word to say by the server",
                content = [(Content(mediaType = MediaType.TEXT_PLAIN_VALUE))]
            )
        ]
    )
    @GetMapping("/say/{word}")
    fun say(@Parameter(description = "The word to say") @PathVariable @NotNull word: String): String {
        log.info("Call say")
        return "Esy server: $word"
    }

    @Operation(summary = "Test authentication")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [(Content(mediaType = MediaType.TEXT_PLAIN_VALUE))]
            )
        ]
    )
    @GetMapping("/auth")
    fun auth(): String {
        log.info("Call auth")
        return "You are authentified"
    }

}
