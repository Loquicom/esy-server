package com.loqui.esy.data.mapper

import com.loqui.esy.data.dto.BankDTO
import com.loqui.esy.data.entity.Bank
import com.loqui.esy.data.mapper.bank.toDTO
import com.loqui.esy.data.mapper.bank.toEntity
import com.loqui.esy.maker.bank.makeBank
import com.loqui.esy.maker.bank.makeBankDTO

class BankMapperTest : MapperTest<Bank, BankDTO>() {

    override fun mapEntity(dto: BankDTO): Bank {
        return toEntity(dto)
    }

    override fun mapEntity(dto: List<BankDTO>): List<Bank> {
        return toEntity(dto)
    }

    override fun mapDTO(entity: Bank): BankDTO {
        return toDTO(entity)
    }

    override fun mapDTO(entity: List<Bank>): List<BankDTO> {
        return toDTO(entity)
    }

    override fun makeEntity(indice: Int): Bank {
        return makeBank(indice)
    }

    override fun makeDTO(indice: Int): BankDTO {
        return makeBankDTO(indice)
    }

}
