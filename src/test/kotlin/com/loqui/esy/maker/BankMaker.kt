package com.loqui.esy.maker.bank

import com.loqui.esy.data.dto.BankDTO
import com.loqui.esy.data.entity.Bank
import com.loqui.esy.data.request.BankRequest
import com.loqui.esy.maker.getPrefix
import com.loqui.esy.maker.toUUID
import com.loqui.esy.maker.user.makeUser
import java.util.*

const val ID = "66ee3342-2d2b-4622-a5dd-1a1e2430fb05"
const val NAME = "Test bank"
const val DESCRIPTION = "Test description for $NAME"
const val ERROR = "Test error message (bank)"

fun makeBank(isMain: Boolean = false): Bank {
    return makeBank(null, isMain)
}

fun makeBank(i: Int? = null, isMain: Boolean = false): Bank {
    return makeBank(ID, i, isMain)
}

fun makeBank(id: String, i: Int? = null, isMain: Boolean = false): Bank {
    val uuid = toUUID(id)
    return makeBank(uuid, i, isMain)
}

fun makeBank(uuid: UUID, i: Int? = null, isMain: Boolean = false): Bank {
    val prefix = getPrefix(i)
    val main = if (isMain) true else !(i != null && i > 1)
    val user = makeUser(i)
    return Bank(
        uuid,
        "$prefix$NAME",
        "$prefix$DESCRIPTION",
        main,
        user
    )
}

fun makeBankDTO(isMain: Boolean = false): BankDTO {
    return makeBankDTO(null, isMain)
}

fun makeBankDTO(i: Int? = null, isMain: Boolean = false): BankDTO {
    return makeBankDTO(ID, i, isMain)
}

fun makeBankDTO(id: String, i: Int? = null, isMain: Boolean = false): BankDTO {
    val uuid = toUUID(id)
    return makeBankDTO(uuid, i, isMain)
}

fun makeBankDTO(uuid: UUID, i: Int? = null, isMain: Boolean = false): BankDTO {
    val prefix = getPrefix(i)
    val main = if (isMain) true else !(i != null && i > 1)
    return BankDTO(
        uuid,
        "$prefix$NAME",
        "$prefix$DESCRIPTION",
        main,
    )
}

fun makeBankRequest(i: Int? = null): BankRequest {
    val prefix = getPrefix(i)
    val main = !(i != null && i > 1)
    val user = makeUser(i).id
    return BankRequest(
        "$prefix$NAME",
        "$prefix$DESCRIPTION",
        main,
        user
    )
}
