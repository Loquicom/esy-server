package com.loqui.esy.config.security

import com.loqui.esy.config.security.filter.JWTFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Autowired private val esyUserDetailService: EsyUserDetailService,
    @Autowired private val jwtFilter: JWTFilter,
    @Value("\${api.path.root}") private val apiRootPath: String
) {

    @Bean
    fun authenticationManager(auth: AuthenticationConfiguration): AuthenticationManager {
        return auth.authenticationManager
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // Enable CORS and disable CSRF
        http.cors().and().csrf().disable()

        // Set session management to stateless
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Set unauthorized requests exception handler
        http.exceptionHandling()
            .authenticationEntryPoint { request: HttpServletRequest?, response: HttpServletResponse, ex: AuthenticationException ->
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ex.message
                )
            }

        // Set user details
        http.userDetailsService(esyUserDetailService)

        // Set permissions on endpoints
        http.authorizeHttpRequests()
            .antMatchers("/openapi/**").permitAll()
            .antMatchers("${this.apiRootPath}/ping").permitAll()
            .antMatchers("${this.apiRootPath}/say/**").permitAll()
            .antMatchers("${this.apiRootPath}/users/register").permitAll()
            .antMatchers("${this.apiRootPath}/users/login").permitAll()
            .anyRequest().authenticated()

        // Add JWT token filter
        http.addFilterBefore(
            jwtFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )

        return http.build()
    }

    // Used by spring security if CORS is enabled.
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOriginPatterns = Collections.singletonList("*")
        config.allowedHeaders = Collections.singletonList("*")
        config.allowedMethods = Collections.singletonList("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
