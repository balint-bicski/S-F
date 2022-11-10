package com.doublefree.validation

import com.fasterxml.jackson.annotation.JsonProperty

class ValidationExceptionDto(@JsonProperty("errors") vararg val errors: ValidationErrorDto) : RuntimeException(
    "Validation failed. " + getErrorsString(errors.toList())
) {

    companion object {
        fun ofError(errorKey: ValidationErrorCode) = ValidationExceptionDto(ValidationErrorDto.error(errorKey))

        fun ofError(errorKey: ValidationErrorCode, params: Map<String, String>) =
            ValidationExceptionDto(ValidationErrorDto.error(errorKey, params))

        private fun getErrorsString(items: List<ValidationErrorDto>) =
            items.joinToString(", ", "{", "}") { error -> error.messageKey.name }
    }
}
