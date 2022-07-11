package com.loqui.esy.entry.validator

import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.exception.EsyValidatorException

abstract class Validator<T> {

    private var prefix: String = ""
    protected val errors = ArrayList<String>()
    protected var valid = true

    /**
     * Verfie si l'objet passé en parametre est valide à l'aide d'assertion
     *
     * @param data L'objet à tester
     */
    protected abstract fun validate(data: T)

    protected fun clear() {
        valid = true
        errors.clear()
    }

    protected fun <D> assertThat(data: D): AssertValidator<D> {
        return AssertValidator(this, data)
    }

    fun isValid(data: T): Boolean {
        clear()
        validate(data)
        return valid
    }

    fun isValid(data: List<T>): Boolean {
        clear()
        for (i in data.indices) {
            prefix = "$i: "
            validate(data[i])
        }
        return valid
    }

    @Throws(EsyValidatorException::class)
    fun isValidOrThrow(data: T, err: EsyError = EsyError.BAD_REQUEST) {
        if (!isValid(data)) {
            throw EsyValidatorException(err).trace(errors)
        }
    }

    @Throws(EsyValidatorException::class)
    fun isValidOrThrow(data: List<T>, err: EsyError = EsyError.BAD_REQUEST) {
        if (!isValid(data)) {
            throw EsyValidatorException(err).trace(errors)
        }
    }

    fun getNumberOfErrors(): Int {
        return errors.size
    }

    fun getError(index: Int): String {
        if (index < 0 || index >= errors.size) {
            throw IllegalArgumentException("Invalid index: $index")
        }
        return errors[index]
    }

    fun getErrors(): List<String> {
        return errors
    }

    class AssertValidator<D>(
        private val validator: Validator<*>,
        private val data: D,
    ) {

        private var valid = true

        fun isNull(): AssertValidator<D> {
            if (data != null) {
                valid = false
            }
            return this
        }

        fun isNotNull(): AssertValidator<D> {
            if (data == null) {
                valid = false
            }
            return this
        }

        fun isEquals(obj: Any): AssertValidator<D> {
            val result = data?.equals(obj)
            if (result == null || !result) {
                valid = false
            }
            return this
        }

        fun isNotEquals(obj: Any): AssertValidator<D> {
            val result = data?.equals(obj)
            if (result == null || result) {
                valid = false
            }
            return this
        }

        fun isBlank(): AssertValidator<D> {
            if (data !is String || (data as String).isNotBlank()) {
                valid = false
            }
            return this
        }

        fun isNotBlank(): AssertValidator<D> {
            if (data !is String || (data as String).isBlank()) {
                valid = false
            }
            return this
        }

        fun isTrue(): AssertValidator<D> {
            if (data !is Boolean || !data) {
                valid = false
            }
            return this
        }

        fun isFalse(): AssertValidator<D> {
            if (data !is Boolean || data) {
                valid = false
            }
            return this
        }

        fun valid() {
            validator.valid = validator.valid && this.valid
        }

        fun orError(message: String) {
            valid()
            if (!valid) {
                validator.errors.add(message)
            }
        }

        @Throws(EsyValidatorException::class)
        fun orThrow(err: EsyError = EsyError.BAD_REQUEST) {
            valid()
            if (!valid) {
                throw EsyValidatorException(err)
            }
        }

    }

}
