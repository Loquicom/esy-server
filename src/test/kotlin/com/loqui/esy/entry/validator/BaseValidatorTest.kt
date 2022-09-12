package com.loqui.esy.entry.validator

import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.exception.EsyException
import com.loqui.esy.exception.EsyValidatorException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class BaseValidatorTest : Validator<Any>() {

    private var validate = true

    @BeforeEach
    fun beforeEach() {
        validate = true
        clear()
    }

    @Test
    fun clearTest() {
        Assertions.assertThat(valid).isTrue
        Assertions.assertThat(errors.size).isEqualTo(0)
        assertThat(false).isTrue().orError("test")
        Assertions.assertThat(valid).isFalse
        Assertions.assertThat(errors.size).isEqualTo(1)
        clear()
        Assertions.assertThat(valid).isTrue
        Assertions.assertThat(errors.size).isEqualTo(0)
    }

    @Test
    fun isValidTest() {
        Assertions.assertThat(isValid("test")).isTrue
        validate = false
        Assertions.assertThat(isValid("test")).isFalse
    }

    @Test
    fun isValidListTest() {
        val array = listOf("test-1", "test-2")
        Assertions.assertThat(isValid(array)).isTrue
        validate = false
        Assertions.assertThat(isValid(array)).isFalse
    }

    @Test
    fun isValidOrThrowTest() {
        isValidOrThrow("test", EsyError.UNKNOWN)
        validate = false
        val ex = assertThrows<EsyValidatorException> { isValidOrThrow("test", EsyError.UNKNOWN) }
        Assertions.assertThat(ex.code).isEqualTo(EsyError.UNKNOWN.code)
        Assertions.assertThat(ex.message).isEqualTo(EsyError.UNKNOWN.message)
        Assertions.assertThat(ex.status).isEqualTo(EsyError.UNKNOWN.status)
    }

    @Test
    fun isValidOrThrowListTest() {
        val array = listOf("test-1", "test-2")
        isValidOrThrow(array, EsyError.UNKNOWN)
        validate = false
        val ex = assertThrows<EsyValidatorException> { isValidOrThrow(array, EsyError.UNKNOWN) }
        Assertions.assertThat(ex.code).isEqualTo(EsyError.UNKNOWN.code)
        Assertions.assertThat(ex.message).isEqualTo(EsyError.UNKNOWN.message)
        Assertions.assertThat(ex.status).isEqualTo(EsyError.UNKNOWN.status)
    }

    @Test
    fun getNumberOfErrorsTest() {
        Assertions.assertThat(getNumberOfErrors()).isEqualTo(0)
        assertThat(false).isTrue().valid()
        Assertions.assertThat(getNumberOfErrors()).isEqualTo(0)
        assertThat(false).isTrue().orError("test-1")
        Assertions.assertThat(getNumberOfErrors()).isEqualTo(1)
        assertThat(false).isTrue().orError("test-2")
        assertThat(false).isTrue().orError("test-3")
        Assertions.assertThat(getNumberOfErrors()).isEqualTo(3)
        clear()
        Assertions.assertThat(getNumberOfErrors()).isEqualTo(0)
    }

    @Test
    fun getErrorTest() {
        assertThrows<IllegalArgumentException> { getError(0) }
        assertThat(false).isTrue().orError("test-1")
        assertThat(false).isTrue().orError("test-2")
        Assertions.assertThat(getError(0)).isEqualTo("test-1")
        Assertions.assertThat(getError(1)).isEqualTo("test-2")
        assertThrows<IllegalArgumentException> { getError(2) }
        assertThrows<IllegalArgumentException> { getError(-1) }
    }

    @Test
    fun getErrorsTest() {
        Assertions.assertThat(getErrors()).isEqualTo(ArrayList<String>())
        assertThat(false).isTrue().orError("test-1")
        assertThat(false).isTrue().orError("test-2")
        val errors = getErrors()
        Assertions.assertThat(errors.size).isEqualTo(2)
        Assertions.assertThat(errors.contains("test-1")).isTrue
        Assertions.assertThat(errors.contains("test-2")).isTrue
    }

    @Test
    fun assertIsNullTest() {
        assertThat(null).isNull().valid()
        Assertions.assertThat(this.valid).isTrue
        clear()
        assertThat("test").isNull().valid()
        Assertions.assertThat(this.valid).isFalse
    }

    @Test
    fun assertIsNotNullTest() {
        assertThat(null).isNotNull().valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat("test").isNotNull().valid()
        Assertions.assertThat(this.valid).isTrue
    }

    @Test
    fun assertIsEqualsTest() {
        assertThat("test").isEquals("test").valid()
        Assertions.assertThat(this.valid).isTrue
        clear()
        assertThat("test").isEquals("not test").valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat(null).isEquals("test").valid()
        Assertions.assertThat(this.valid).isFalse
    }

    @Test
    fun assertIsNotEqualsTest() {
        assertThat("test").isNotEquals("test").valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat("test").isNotEquals("not test").valid()
        Assertions.assertThat(this.valid).isTrue
        clear()
        assertThat(null).isEquals("test").valid()
        Assertions.assertThat(this.valid).isFalse
    }

    @Test
    fun assertIsBlankTest() {
        assertThat("").isBlank().valid()
        Assertions.assertThat(this.valid).isTrue
        clear()
        assertThat("                ").isBlank().valid()
        Assertions.assertThat(this.valid).isTrue
        clear()
        assertThat("test").isBlank().valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat(null).isBlank().valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat(Any()).isBlank().valid()
        Assertions.assertThat(this.valid).isFalse
    }

    @Test
    fun assertIsNotBlankTest() {
        assertThat("").isNotBlank().valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat("                ").isNotBlank().valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat("test").isNotBlank().valid()
        Assertions.assertThat(this.valid).isTrue
        clear()
        assertThat(null).isNotBlank().valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat(Any()).isNotBlank().valid()
        Assertions.assertThat(this.valid).isFalse
    }

    @Test
    fun assertIsTrueTest() {
        assertThat(true).isTrue().valid()
        Assertions.assertThat(this.valid).isTrue
        clear()
        assertThat(false).isTrue().valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat("test").isTrue().valid()
        Assertions.assertThat(this.valid).isFalse
    }

    @Test
    fun assertIsFalseTest() {
        assertThat(true).isFalse().valid()
        Assertions.assertThat(this.valid).isFalse
        clear()
        assertThat(false).isFalse().valid()
        Assertions.assertThat(this.valid).isTrue
        clear()
        assertThat("test").isFalse().valid()
        Assertions.assertThat(this.valid).isFalse
    }

    @Test
    fun validTest() {
        Assertions.assertThat(valid).isTrue
        assertThat(true).isTrue().valid()
        Assertions.assertThat(valid).isTrue
        assertThat(false).isTrue().valid()
        Assertions.assertThat(valid).isFalse
    }

    @Test
    fun assertOrErrorTest() {
        assertThat(true).isTrue().orError("test-1")
        assertThat(false).isTrue().orError("test-2")
        assertThat(false).isTrue().orError("test-3")
        Assertions.assertThat(errors.size).isEqualTo(2)
        Assertions.assertThat(errors.contains("test-1")).isFalse
        Assertions.assertThat(errors.contains("test-2")).isTrue
        Assertions.assertThat(errors.contains("test-3")).isTrue
    }

    @Test
    fun assertOrThrowTest() {
        assertThat(true).isTrue().orThrow(EsyError.UNKNOWN)
        clear()
        val ex = assertThrows<EsyValidatorException> { assertThat(false).isTrue().orThrow(EsyError.UNKNOWN) }
        Assertions.assertThat(ex.code).isEqualTo(EsyError.UNKNOWN.code)
        Assertions.assertThat(ex.message).isEqualTo(EsyError.UNKNOWN.message)
        Assertions.assertThat(ex.status).isEqualTo(EsyError.UNKNOWN.status)
    }

    @Test
    fun assertThatThrowTest() {
        val errMsg = "message-test"
        clear()
        assertThatThrow({ throw EsyException("test") }, errMsg)
        Assertions.assertThat(valid).isTrue
        Assertions.assertThat(errors.size).isEqualTo(0)
        clear()
        assertThatThrow({ throw EsyException("test") }, EsyException::class, errMsg)
        Assertions.assertThat(valid).isTrue
        Assertions.assertThat(errors.size).isEqualTo(0)
        clear()
        assertThatThrow({ throw EsyValidatorException("test") }, EsyException::class, errMsg)
        Assertions.assertThat(valid).isTrue
        Assertions.assertThat(errors.size).isEqualTo(0)
        clear()
        assertThatThrow({/* None */ }, errMsg)
        Assertions.assertThat(valid).isFalse
        Assertions.assertThat(errors.size).isEqualTo(1)
        Assertions.assertThat(errors[0]).isEqualTo(errMsg)
        clear()
        assertThatThrow({/* None */ }, EsyException::class, errMsg)
        Assertions.assertThat(valid).isFalse
        Assertions.assertThat(errors.size).isEqualTo(1)
        Assertions.assertThat(errors[0]).isEqualTo(errMsg)
        clear()
        assertThatThrow({ throw EsyException("test") }, EsyValidatorException::class, errMsg)
        Assertions.assertThat(valid).isFalse
        Assertions.assertThat(errors.size).isEqualTo(1)
        Assertions.assertThat(errors[0]).isEqualTo(errMsg)
    }

    @Test
    fun assertThatNotThrowTest() {
        val errMsg = "message-test"
        clear()
        assertThatNotThrow({/* None */ }, errMsg)
        Assertions.assertThat(valid).isTrue
        Assertions.assertThat(errors.size).isEqualTo(0)
        clear()
        assertThatNotThrow({ throw EsyException("test") }, errMsg)
        Assertions.assertThat(valid).isFalse
        Assertions.assertThat(errors.size).isEqualTo(1)
        Assertions.assertThat(errors[0]).isEqualTo(errMsg)
    }

    override fun validate(data: Any) {
        assertThat(validate).isTrue().valid()
    }
}
