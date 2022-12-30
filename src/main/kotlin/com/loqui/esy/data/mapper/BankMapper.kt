package com.loqui.esy.data.mapper.bank

import com.loqui.esy.data.dto.BankDTO
import com.loqui.esy.data.entity.Bank
import com.loqui.esy.data.entity.User
import com.loqui.esy.utils.DEFAULT_ROLE
import com.loqui.esy.utils.EMAIL_FOR_DTO
import com.loqui.esy.utils.LOGIN_FOR_DTO
import com.loqui.esy.utils.PASSWORD_FOR_DTO
import java.util.*

fun toDTO(entity: Bank): BankDTO {
    return BankDTO(entity.id, entity.name, entity.description, entity.main)
}

fun toDTO(entity: List<Bank>): List<BankDTO> {
    return entity.map { elt -> toDTO(elt) }
}

fun toEntity(dto: BankDTO): Bank {
    val noUser = User(UUID.randomUUID(), LOGIN_FOR_DTO, PASSWORD_FOR_DTO, EMAIL_FOR_DTO, DEFAULT_ROLE, false)
    return Bank(dto.id, dto.name, dto.description, dto.main, noUser)
}

fun toEntity(dto: List<BankDTO>): List<Bank> {
    return dto.map { elt -> toEntity(elt) }
}
