package com.loqui.esy.service

import com.loqui.esy.data.entity.User
import com.loqui.esy.data.request.LoginRequest
import com.loqui.esy.data.request.RegisterRequest
import com.loqui.esy.data.view.LoginView
import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.exception.EsyAuthenticationException
import com.loqui.esy.exception.EsyNotFoundException
import com.loqui.esy.repository.UserRepository
import com.loqui.esy.utils.DEFAULT_ROLE
import com.loqui.esy.utils.JWTUtils
import com.loqui.esy.utils.mapper.user.toDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(
    @Value("\${esy.security.register}") private val registerActive: Boolean,
    @Autowired private val repository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val jwtUtils: JWTUtils
) {

    private val log = LoggerFactory.getLogger(UserService::class.java)

    @Throws(EsyAuthenticationException::class)
    fun register(request: RegisterRequest): LoginView {
        log.debug("register, RegisterRequest={}", request)
        // Check if register is enabled
        if (!registerActive) {
            log.info("Register is disabled")
            throw EsyAuthenticationException(EsyError.REGISTER_DISABLED)
        }
        // Check if user exist
        val optUser = repository.findByLogin(request.login)
        if (optUser.isPresent) {
            log.info("User ${request.login} already exist")
            throw EsyAuthenticationException(EsyError.REGISTER_LOGIN_ALREADY_EXIST)
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
        log.info("New user ${request.login} saved")
        val token = jwtUtils.generate(toDTO(user))
        return LoginView(token)
    }

    @Throws(EsyAuthenticationException::class)
    fun login(request: LoginRequest): LoginView {
        log.debug("login, LoginRequest={}", request)
        try {
            val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.login, request.password))
            val user: User = authentication.principal as User
            val token = jwtUtils.generate(toDTO(user))
            return LoginView(token)
        } catch (ex: BadCredentialsException) {
            log.info("Bad credentials for ${request.login}")
            throw EsyAuthenticationException(EsyError.AUTHENTICATION, ex)
        }

    }

    fun validate(token: String): Boolean {
        log.debug("validate, token={}", token)
        if (!jwtUtils.verify(token)) {
            return false
        }
        val userDto = jwtUtils.getUser(token)
        val optUser = repository.findById(userDto.id)
        if (optUser.isEmpty) {
            return false
        }
        val user = optUser.get()
        return user.enabled
    }

    @Throws(EsyAuthenticationException::class)
    fun refresh(token: String): LoginView {
        log.debug("refresh, token={}", token)
        val login = SecurityContextHolder.getContext().authentication.principal as String
        val user = jwtUtils.getUser(token)
        if (login != user.login) {
            log.info("Login information mismatch between token ({}) and security context ({})", user.login, login)
            throw EsyAuthenticationException("Token login mismatch with security login", HttpStatus.BAD_REQUEST)
        }
        val newToken = jwtUtils.refresh(token)
        return LoginView(newToken)
    }

    @Throws(EsyNotFoundException::class)
    fun get(id: UUID): User {
        log.debug("get, id={}", id)
        val optionalUser = repository.findById(id)
        return optionalUser.orElseThrow { EsyNotFoundException("Unable to find user with id=${id}") }
    }

    @Throws(EsyNotFoundException::class)
    fun getConnected(): User {
        val login = SecurityContextHolder.getContext().authentication.principal as String
        val optUser = repository.findByLogin(login)
        return optUser.orElseThrow { EsyNotFoundException("Unable to find user with login=${login}") }
    }

}
