package com.loqui.esy.entry.validator

import com.loqui.esy.data.request.BankRequest
import com.loqui.esy.maker.bank.makeBankRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

class BankValidatorTest : ValidatorTest() {

    @Autowired
    private lateinit var validator: BankValidator

    @MockBean
    private lateinit var uuidValidator: UuidValidator

    @Test
    fun contextLoad() {
        Assertions.assertThat(validator).isNotNull
    }

    @Test
    fun validTest() {
        val request = makeBankRequest()
        Mockito.`when`(uuidValidator.isValid(request.user.toString())).thenReturn(true)
        Assertions.assertThat(validator.isValid(request)).isTrue
    }

    @Test
    fun validListTest() {
        val request = ArrayList<BankRequest>()
        for (i in 1..3) {
            request.add(makeBankRequest(i))
        }
        Mockito.`when`(uuidValidator.isValid(Mockito.anyString())).thenReturn(true)
        Assertions.assertThat(validator.isValid(request)).isTrue
    }

    @Test
    fun notValidTest() {
        val request = BankRequest(
            "",
            "",
            false,
            UUID.randomUUID()
        )
        Mockito.`when`(uuidValidator.isValid(Mockito.anyString())).thenReturn(false)

        Assertions.assertThat(validator.isValid(request)).isFalse
        Assertions.assertThat(validator.getNumberOfErrors()).isEqualTo(2)
        Assertions.assertThat(validator.getErrors().contains(BankValidator.BankValidatorConstant.NAME_REQUIRED)).isTrue
        Assertions.assertThat(validator.getErrors().contains(BankValidator.BankValidatorConstant.UUID_INVALID)).isTrue
    }

    @Test
    fun notValidListTest() {
        val request = ArrayList<BankRequest>()
        request.add(BankRequest("", "", false, UUID.randomUUID()))
        request.add(BankRequest("     ", "      ", true, UUID.randomUUID()))
        Mockito.`when`(uuidValidator.isValid(Mockito.anyString())).thenReturn(false)

        Assertions.assertThat(validator.isValid(request)).isFalse
        Assertions.assertThat(validator.getNumberOfErrors()).isEqualTo(4)
        Assertions.assertThat(validator.getErrors().contains("1: " + BankValidator.BankValidatorConstant.NAME_REQUIRED)).isTrue
        Assertions.assertThat(validator.getErrors().contains("1: " + BankValidator.BankValidatorConstant.UUID_INVALID)).isTrue
        Assertions.assertThat(validator.getErrors().contains("2: " + BankValidator.BankValidatorConstant.NAME_REQUIRED)).isTrue
        Assertions.assertThat(validator.getErrors().contains("2: " + BankValidator.BankValidatorConstant.UUID_INVALID)).isTrue
    }

}
