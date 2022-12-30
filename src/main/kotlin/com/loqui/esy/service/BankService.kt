package com.loqui.esy.service

import com.loqui.esy.data.entity.Bank
import com.loqui.esy.data.request.BankRequest
import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.exception.EsyNotFoundException
import com.loqui.esy.exception.EsyUnauthorizedException
import com.loqui.esy.repository.BankRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BankService(
    @Autowired private val repository: BankRepository,
    @Autowired private val userService: UserService
) {

    private val log = LoggerFactory.getLogger(BankService::class.java)

    @Throws(EsyNotFoundException::class, EsyUnauthorizedException::class)
    fun get(id: UUID): Bank {
        log.debug("get: UUID={}", id)
        // Get the connected user
        val user = userService.getConnected()
        // Get the bank info
        val optBank = repository.findById(id)
        val bank = optBank.orElseThrow { EsyNotFoundException("Unable to find bank with id=${id}") }
        // Check bank owner
        if (bank.user.id != user.id) {
            throw EsyUnauthorizedException(EsyError.UNAUTHORIZED)
        }
        // Return bank
        return bank
    }

    fun add(request: BankRequest): Bank {
        log.debug("add: BankRequest={}", request)
        // Get the user
        val user = userService.get(request.user)
        // Create the new bank
        val bank = Bank(
            UUID.randomUUID(),
            request.name,
            request.description,
            request.main,
            user
        )
        return save(bank)
    }

    @Throws(EsyNotFoundException::class, EsyUnauthorizedException::class)
    fun update(id: UUID, request: BankRequest): Bank {
        // TODO vérifier que l'utilisateur possède la banque
        log.debug("update: UUID={}, BankRequest={}", id, request)
        // Get the bank
        val optionalBank = repository.findById(id)
        val bank = optionalBank.orElseThrow { EsyNotFoundException("Unable to find bank with id=${id}") }
        // Edit bank info
        bank.name = request.name
        bank.description = request.description
        bank.main = request.main
        // Save and return
        return save(bank)
    }

    @Throws(EsyNotFoundException::class, EsyUnauthorizedException::class)
    fun delete(id: UUID) {
        // TODO vérifier que l'utilisateur possède la banque
        log.debug("delete: UUID={}", id)
        repository.deleteById(id)
    }

    private fun save(bank: Bank): Bank {
        // If it's the new main bank account check if one alreday exist
        var banks: List<Bank>? = null
        if (bank.main) {
            banks = repository.findByUser_IdAndMain(bank.user.id)
        }
        // save the bank
        val savedBank = repository.save(bank)
        // If they are other main bank remove the main statut
        if (!banks.isNullOrEmpty()) {
            banks.forEach {
                // Only edit if it's not the current bank
                if (it.id != bank.id) {
                    log.info("Set main to false on bank ${it.name} (${it.id})")
                    it.main = false
                    repository.save(it)
                }
            }
        }
        // Return the saved bank
        return savedBank
    }

}
