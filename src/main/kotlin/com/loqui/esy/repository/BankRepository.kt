package com.loqui.esy.repository

import com.loqui.esy.data.entity.Bank
import org.springframework.data.repository.CrudRepository
import java.util.*

interface BankRepository : CrudRepository<Bank, UUID> {

    fun findByUser_IdAndMain(user_id: UUID, main: Boolean = true): List<Bank>

}
