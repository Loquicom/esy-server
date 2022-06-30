package com.loqui.esy.entry.security

import com.loqui.esy.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

@Component
class EsyUserDetailService(
    @Autowired private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val optUser = username?.let { userRepository.findByLogin(username) } ?: throw UsernameNotFoundException("No username provided")
        val user = optUser.orElseThrow { UsernameNotFoundException("Could not find user with login: $username") }
        return User(user.login, user.password, Collections.singletonList(SimpleGrantedAuthority("ROLE_USER")))
    }

}
