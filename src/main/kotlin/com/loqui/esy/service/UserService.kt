package com.loqui.esy.service

import com.loqui.esy.data.entity.User
import com.loqui.esy.data.request.LoginRequest
import com.loqui.esy.data.request.RegisterRequest
import com.loqui.esy.data.view.LoginView
import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.exception.EsyAuthentificationException
import com.loqui.esy.repository.UserRepository
import com.loqui.esy.utils.DEFAULT_ROLE
import com.loqui.esy.utils.JWTUtil
import com.loqui.esy.utils.mapper.user.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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

    @Throws(EsyAuthentificationException::class)
    fun register(request: RegisterRequest): LoginView {
        // Check if user exist
        val optUser = repository.findByLogin(request.login)
        if (optUser.isPresent) {
            throw EsyAuthentificationException(EsyError.REGISTER_LOGIN_ALREADY_EXIST)
        }
        // Create user
        val password = passwordEncoder.encode(request.password)
        val user = User(
            UUID.randomUUID(),
            request.login,
            password,
            request.email,
            DEFAULT_ROLE
        )
        repository.save(user)
        val token = jwtUtil.generate(toDTO(user))
        return LoginView(token)
    }

    @Throws(EsyAuthentificationException::class)
    fun login(request: LoginRequest): LoginView {
        try {
            val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.login, request.password))
            val user: User = authentication.principal as User
            val token = jwtUtil.generate(toDTO(user))
            return LoginView(token)
        } catch (ex: BadCredentialsException) {
            throw EsyAuthentificationException(EsyError.AUTHENTIFICATION, ex)
        }

    }

}
