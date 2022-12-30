package com.loqui.esy.entry.controller

import com.loqui.esy.data.dto.BankDTO
import com.loqui.esy.data.mapper.bank.toDTO
import com.loqui.esy.data.request.BankRequest
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.data.view.SuccessView
import com.loqui.esy.entry.validator.BankValidator
import com.loqui.esy.entry.validator.UuidValidator
import com.loqui.esy.service.BankService
import com.loqui.esy.utils.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
@RequestMapping("\${esy.api.root-path}/banks")
class BankController(
    @Autowired private val service: BankService,
    @Autowired private val bankValidator: BankValidator,
    @Autowired private val uuidValidator: UuidValidator
) {

    @GetMapping("{id}")
    fun get(@PathVariable id: String): ResponseEntity<ResponseView<BankDTO>> {
        uuidValidator.isValidOrThrow(id)
        val result = service.get(UUID.fromString(id))
        return response(toDTO(result))
    }

    @PostMapping
    fun add(@RequestBody request: BankRequest): ResponseEntity<ResponseView<BankDTO>> {
        bankValidator.isValidOrThrow(request)
        val result = service.add(request)
        return response(toDTO(result))
    }

    @PutMapping("{id}")
    fun update(@PathVariable id: String, @RequestBody request: BankRequest): ResponseEntity<ResponseView<BankDTO>> {
        uuidValidator.isValidOrThrow(id)
        bankValidator.isValidOrThrow(request)
        val result = service.update(UUID.fromString(id), request)
        return response(toDTO(result))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: String): ResponseEntity<SuccessView> {
        uuidValidator.isValidOrThrow(id)
        service.delete(UUID.fromString(id))
        return response(true)
    }

}
