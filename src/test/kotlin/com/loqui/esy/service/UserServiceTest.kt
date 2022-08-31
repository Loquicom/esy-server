package com.loqui.esy.service

import com.loqui.esy.data.entity.User
import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.exception.EsyAuthenticationException
import com.loqui.esy.maker.*
import com.loqui.esy.maker.impl.TestAuthentication
import com.loqui.esy.maker.impl.TestContext
import com.loqui.esy.repository.UserRepository
import com.loqui.esy.utils.JWTUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest : ServiceTest() {

    @Autowired
    private lateinit var service: UserService

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder

    @MockBean
    private lateinit var authenticationManager: AuthenticationManager

    @MockBean
    private lateinit var jwtUtils: JWTUtils

    @BeforeAll
    fun beforeAll() {
        val user = makeUser()
        val context = TestContext()
        Mockito.mockStatic(UUID::class.java).`when`<UUID>(UUID::randomUUID).thenReturn(user.id)
        Mockito.mockStatic(SecurityContextHolder::class.java).`when`<SecurityContext>(SecurityContextHolder::getContext).thenReturn(context)
    }

    @Test
    fun contextLoad() {
        assertThat(service).isNotNull
    }

    @Test
    fun registerSuccessTest() {
        val request = makeRegisterRequest()
        val user = makeUser()
        val userDto = convertUser(user)
        val expected = makeLoginView()

        Mockito.`when`(userRepository.findByLogin(request.login)).thenReturn(Optional.empty())
        Mockito.`when`(passwordEncoder.encode(Mockito.anyString())).thenReturn(PASSWORD_ENCODED)
        Mockito.`when`(userRepository.save(user)).thenReturn(user)
        Mockito.`when`(jwtUtils.generate(userDto)).thenReturn(JWT_TOKEN)

        val result = service.register(request)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun registerFailTest() {
        val request = makeRegisterRequest()
        val user = makeUser()

        Mockito.`when`(userRepository.findByLogin(request.login)).thenReturn(Optional.of(user))

        val ex = assertThrows<EsyAuthenticationException> { service.register(request) }

        assertThat(ex.code).isEqualTo(EsyError.REGISTER_LOGIN_ALREADY_EXIST.code)
        assertThat(ex.message).isEqualTo(EsyError.REGISTER_LOGIN_ALREADY_EXIST.message)
        assertThat(ex.status).isEqualTo(EsyError.REGISTER_LOGIN_ALREADY_EXIST.status)
        assertThat(ex.trace()).isEmpty()
    }

    @Test
    fun loginSuccessTest() {
        val request = makeLoginRequest()
        val userDto = makeUserDTO(ID)
        val expected = makeLoginView()

        Mockito.`when`(authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.login, request.password)))
            .thenReturn(TestAuthentication())
        Mockito.`when`(jwtUtils.generate(userDto)).thenReturn(JWT_TOKEN)

        val result = service.login(request)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun loginFailTest() {
        val request = makeLoginRequest()

        Mockito.`when`(authenticationManager.authenticate(Mockito.any())).thenThrow(BadCredentialsException::class.java)

        val ex = assertThrows<EsyAuthenticationException> { service.login(request) }

        assertThat(ex.code).isEqualTo(EsyError.AUTHENTICATION.code)
        assertThat(ex.message).isEqualTo(EsyError.AUTHENTICATION.message)
        assertThat(ex.status).isEqualTo(EsyError.AUTHENTICATION.status)
        assertThat(ex.trace()).isEmpty()
        assertThat(ex.throwable).isNotNull
        assertThat(ex.throwable?.javaClass).isEqualTo(BadCredentialsException::class.java)
    }

    @Test
    fun validateSuccessTest() {
        val userDto = makeUserDTO()
        val user = makeUser()

        Mockito.`when`(jwtUtils.verify(JWT_TOKEN)).thenReturn(true)
        Mockito.`when`(jwtUtils.getUser(JWT_TOKEN)).thenReturn(userDto)
        Mockito.`when`(userRepository.findById(userDto.id)).thenReturn(Optional.of(user))

        val result = service.validate(JWT_TOKEN)
        assertThat(result).isTrue
    }

    @Test
    fun validateInvalidTokenTest() {
        Mockito.`when`(jwtUtils.verify(JWT_TOKEN)).thenReturn(false)

        val result = service.validate(JWT_TOKEN)
        assertThat(result).isFalse
    }

    @Test
    fun validateNoUserTest() {
        val userDto = makeUserDTO()

        Mockito.`when`(jwtUtils.verify(JWT_TOKEN)).thenReturn(true)
        Mockito.`when`(jwtUtils.getUser(JWT_TOKEN)).thenReturn(userDto)
        Mockito.`when`(userRepository.findById(userDto.id)).thenReturn(Optional.empty())

        val result = service.validate(JWT_TOKEN)
        assertThat(result).isFalse
    }

    @Test
    fun validateNotEnabledTest() {
        val userDto = makeUserDTO()
        val user = User(
            UUID.randomUUID(),
            LOGIN,
            PASSWORD_ENCODED,
            EMAIL,
            ROLE,
            false
        )

        Mockito.`when`(jwtUtils.verify(JWT_TOKEN)).thenReturn(true)
        Mockito.`when`(jwtUtils.getUser(JWT_TOKEN)).thenReturn(userDto)
        Mockito.`when`(userRepository.findById(userDto.id)).thenReturn(Optional.of(user))

        val result = service.validate(JWT_TOKEN)
        assertThat(result).isFalse
    }

    @Test
    fun refreshSuccessTest() {
        val userDto = makeUserDTO()
        val expected = makeLoginView()

        Mockito.`when`(jwtUtils.getUser(JWT_TOKEN)).thenReturn(userDto)
        Mockito.`when`(jwtUtils.refresh(JWT_TOKEN)).thenReturn(JWT_TOKEN)

        val result = service.refresh(JWT_TOKEN)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun refreshFailTest() {
        val userDto = makeUserDTO(1)

        Mockito.`when`(jwtUtils.getUser(JWT_TOKEN)).thenReturn(userDto)

        val ex = assertThrows<EsyAuthenticationException> { service.refresh(JWT_TOKEN) }

        assertThat(ex.code).isEqualTo(0)
        assertThat(ex.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(ex.trace()).isEmpty()
        assertThat(ex.throwable).isNull()
    }

}
