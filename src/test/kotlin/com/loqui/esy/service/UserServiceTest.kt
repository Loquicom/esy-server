package com.loqui.esy.service

import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.exception.EsyAuthenticationException
import com.loqui.esy.maker.*
import com.loqui.esy.maker.impl.TestAuthentication
import com.loqui.esy.repository.UserRepository
import com.loqui.esy.utils.JWTUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

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
    private lateinit var jwtUtil: JWTUtil

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
        Mockito.mockStatic(UUID::class.java).`when`<UUID>(UUID::randomUUID).thenReturn(user.id)
        Mockito.`when`(userRepository.save(user)).thenReturn(user)
        Mockito.`when`(jwtUtil.generate(userDto)).thenReturn(JWT_TOKEN)

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
        Mockito.`when`(jwtUtil.generate(userDto)).thenReturn(JWT_TOKEN)

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


}
