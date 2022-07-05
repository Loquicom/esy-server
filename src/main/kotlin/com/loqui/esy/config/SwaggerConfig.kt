package com.loqui.esy.config

import io.swagger.v3.oas.models.Components

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme

import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

    @Bean
    fun apiGroup(): GroupedOpenApi {
        return GroupedOpenApi
            .builder()
            .group("Esy API")
            .pathsToMatch("/api/**")
            .build()
    }

    @Bean
    fun apiInfo(): OpenAPI {
        val securitySchemeName = "bearerAuth"
        return OpenAPI()
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
            .info(
                Info()
                    .title("Esy server Rest API")
                    .description("Rest API for esy applications")
                    .version("0.0.1")
            )
    }
}
