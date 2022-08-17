package com.loqui.esy.config.security

import com.loqui.esy.data.entity.User
import com.loqui.esy.maker.LOGIN
import com.loqui.esy.maker.makeUser
import com.loqui.esy.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.context.TestPropertySource
import java.util.*

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class EsyUserDetailServiceTest {

    @Autowired
    private lateinit var userDetailService: EsyUserDetailService

    @MockBean
    private lateinit var userRepository: UserRepository

    @Test
    fun contextLoad() {
        assertThat(userDetailService).isNotNull
    }

    @Test
    fun loadUserByUsernameSuccesTest() {
        val user = makeUser()

        Mockito.`when`(userRepository.findByLogin(LOGIN)).thenReturn(Optional.of(user))

        val result = userDetailService.loadUserByUsername(LOGIN)
        assertThat(result).isInstanceOf(User::class.java)
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun loadUserByUsernameNoUsernameTest() {
        assertThrows<UsernameNotFoundException> { userDetailService.loadUserByUsername(null) }
    }

    @Test
    fun loadUserByUsernameNoUserTest() {
        Mockito.`when`(userRepository.findByLogin(LOGIN)).thenReturn(Optional.empty())

        assertThrows<UsernameNotFoundException> { userDetailService.loadUserByUsername(LOGIN) }
    }

}
