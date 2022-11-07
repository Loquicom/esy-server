package com.loqui.esy.repository

import com.loqui.esy.maker.toUUID
import com.loqui.esy.maker.user.EMAIL
import com.loqui.esy.maker.user.ID
import com.loqui.esy.maker.user.LOGIN
import com.loqui.esy.maker.user.makeUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import java.util.*

class UserRepositoryTest : RepositoryTest() {

    @Autowired
    private lateinit var repository: UserRepository

    @Test
    fun contecxLoad() {
        assertThat(repository).isNotNull
    }

    @Test
    fun findByIdTest() {
        val user = makeUser(ID)
        val uuid = toUUID(ID)
        val result = repository.findById(uuid)
        assertThat(result.isPresent).isTrue
        assertThat(result.get()).isEqualTo(user)
    }

    @Test
    fun findByIdNotExistTest() {
        val result = repository.findById(UUID.randomUUID())
        assertThat(result.isEmpty).isTrue
    }

    @Test
    fun findByLoginTest() {
        val user = makeUser(ID)
        val result = repository.findByLogin(LOGIN)
        assertThat(result.isPresent).isTrue
        assertThat(result.get()).isEqualTo(user)
    }

    @Test
    fun findByLoginNotExistTest() {
        val result = repository.findByLogin("test")
        assertThat(result.isEmpty).isTrue
    }

    @Test
    fun findByEmailTest() {
        val user = makeUser(ID)
        val result = repository.findByEmail(EMAIL)
        assertThat(result.isPresent).isTrue
        assertThat(result.get()).isEqualTo(user)
    }

    @Test
    fun findByEmaimNotExistTest() {
        val result = repository.findByEmail("test")
        assertThat(result.isEmpty).isTrue
    }

    @Test
    fun saveAndCountTest() {
        val user = makeUser(1)
        val actual = repository.count()
        val result = repository.save(user)
        val count = repository.count()
        assertThat(result).isEqualTo(user)
        assertThat(count).isEqualTo(actual + 1)
    }

    @Test
    fun saveLoginAlreadyExistFailTest() {
        val user = makeUser()
        assertThrows<DataIntegrityViolationException> { repository.save(user) }
    }

}
