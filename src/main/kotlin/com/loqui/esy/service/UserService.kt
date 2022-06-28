package com.loqui.esy.service

import com.loqui.esy.repository.UserRepository
import com.loqui.esy.service.dto.UserDTO
import com.loqui.esy.utils.mapper.user.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val repository: UserRepository
) {

    fun getUser(login: String): UserDTO? {
        val optionalUser = repository.findByLogin(login)
        if (optionalUser.isPresent) {
            return null
        }
        val user = optionalUser.get()
        return toDTO(user)
    }

}
