package com.loqui.esy.repository

import com.loqui.esy.repository.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, UUID> {

    fun findByLogin(login: String): Optional<User>

    fun findByEmail(email: String): Optional<User>

}
