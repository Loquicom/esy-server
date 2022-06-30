package com.loqui.esy.service

import com.loqui.esy.repository.UserRepository
import com.loqui.esy.repository.entity.User
import com.loqui.esy.service.dto.LoginRequest
import com.loqui.esy.service.dto.LoginView
import com.loqui.esy.service.dto.RegisterRequest
import com.loqui.esy.utils.JWTUtil
import com.loqui.esy.utils.mapper.user.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(
    @Autowired private val repository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val jwtUtil: JWTUtil
) {

    fun register(request: RegisterRequest): LoginView {
        val password = passwordEncoder.encode(request.password)
        val user = User(
            UUID.randomUUID(),
            request.login,
            password,
            request.email
        )
        repository.save(user)
        val token = jwtUtil.generate(toDTO(user))
        return LoginView(
            token
        )
    }

    fun login(request: LoginRequest): LoginView {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.login, request.password))
        val user: User = authentication.principal as User
        val scope: String = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .toList().joinToString { "," }
        val token = jwtUtil.generate(toDTO(user), scope)
        return LoginView(
            token
        )
    }

}
