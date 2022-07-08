package com.loqui.esy.entry.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("\${esy.api.root-path}")
@CrossOrigin
class RootController {

    @Operation(summary = "Display server info")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Server info",
                content = [(Content(mediaType = "text/plain"))]
            )
        ]
    )
    @GetMapping("/")
    fun info(): String {
        return """
            It's works
            
            Esy server by Loquicom
            Git repository: TODO
            
            Open API: /openapi
            Documentation: /docs
        """.trimIndent()
    }

    @Operation(summary = "Ping the server")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Pong",
                content = [(Content(mediaType = "text/plain"))]
            )
        ]
    )
    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }

    @Operation(summary = "Say one word")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "The word to say by the server",
                content = [(Content(mediaType = "text/plain"))]
            )
        ]
    )
    @GetMapping("/say/{word}")
    fun say(@Parameter(description = "The word to say") @PathVariable @NotNull word: String): String {
        return "Esy server: $word"
    }

    @Operation(summary = "Test authentication")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [(Content(mediaType = "text/plain"))]
            )
        ]
    )
    @GetMapping("/auth")
    fun hello(): String {
        return "You are authentified"
    }

}
