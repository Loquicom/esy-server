package com.loqui.esy.config.security.filter

import com.loqui.esy.config.security.EsyUserDetailService
import com.loqui.esy.exception.EsyTokenException
import com.loqui.esy.utils.JWTUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JWTFilter(
    @Autowired private val userDetailService: EsyUserDetailService,
    @Autowired private val jwtUtil: JWTUtil
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JWTFilter::class.java)

    private fun filter(authHeader: String, response: HttpServletResponse) {
        try {
            val token = authHeader.substring(7)
            if (token.isBlank()) {
                log.info("Token missing in Authorization header")
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Authorization header")
            }
            val user = jwtUtil.getUser(token)
            val detail = userDetailService.loadUserByUsername(user.login)
            val authToken = UsernamePasswordAuthenticationToken(user.login, detail.password, detail.authorities)
            if (SecurityContextHolder.getContext().authentication == null) {
                SecurityContextHolder.getContext().authentication = authToken
            }
        } catch (ex: StringIndexOutOfBoundsException) {
            log.info("Unable to get the token in Authorization header")
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid data in Authorization header")
        } catch (ex: EsyTokenException) {
            log.info("Invalid JWT token in Authorization header")
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Authorization header")
        }
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.isNotBlank() && authHeader.startsWith("Bearer ")) {
            filter(authHeader, response)
        }

        filterChain.doFilter(request, response)
    }
}
