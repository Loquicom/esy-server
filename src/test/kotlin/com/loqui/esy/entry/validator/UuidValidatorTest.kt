package com.loqui.esy.entry.validator

import com.loqui.esy.maker.ID
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class UuidValidatorTest : ValidatorTest() {

    @Autowired
    private lateinit var validator: UuidValidator

    @Test
    fun contextLoad() {
        Assertions.assertThat(validator).isNotNull
    }

    @Test
    fun validTest() {
        Assertions.assertThat(validator.isValid(UUID.randomUUID().toString())).isTrue
        Assertions.assertThat(validator.isValid(ID)).isTrue
    }

    @Test
    fun validListTest() {
        val list = ArrayList<String>()
        list.add(UUID.randomUUID().toString())
        list.add(ID)
        Assertions.assertThat(validator.isValid(list)).isTrue
    }

    @Test
    fun notValidTest() {
        Assertions.assertThat(validator.isValid("is not an uuid")).isFalse
        Assertions.assertThat(validator.getNumberOfErrors()).isEqualTo(1)
        Assertions.assertThat(validator.getError(0)).isEqualTo(UuidValidator.UuidValidatorConstant.UUID_INVALID)
        Assertions.assertThat(validator.isValid(ID.replace("7", "z"))).isFalse
        Assertions.assertThat(validator.getNumberOfErrors()).isEqualTo(1)
        Assertions.assertThat(validator.getError(0)).isEqualTo(UuidValidator.UuidValidatorConstant.UUID_INVALID)
    }

    @Test
    fun notValidListTest() {
        val list = ArrayList<String>()
        list.add("is not an uuid")
        list.add(ID)
        list.add(ID.replace("7", "z"))
        list.add(UUID.randomUUID().toString())
        Assertions.assertThat(validator.isValid(list)).isFalse
        Assertions.assertThat(validator.getNumberOfErrors()).isEqualTo(2)
        Assertions.assertThat(validator.getError(0)).isEqualTo("1: " + UuidValidator.UuidValidatorConstant.UUID_INVALID)
        Assertions.assertThat(validator.getError(1)).isEqualTo("3: " + UuidValidator.UuidValidatorConstant.UUID_INVALID)
    }

}
