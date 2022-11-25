package com.doublefree.validation

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class ValidationErrorDto(
    @JsonProperty("messageKey") val messageKey: ValidationErrorCode,
    @JsonProperty("params") val params: Map<String, String>?
) : Serializable {

    companion object {
        fun error(messageKey: ValidationErrorCode) = error(messageKey, emptyMap())
        fun error(messageKey: ValidationErrorCode, params: Map<String, String>) = ValidationErrorDto(messageKey, params)
    }

}

enum class ValidationErrorCode {
    USER_EXIST_WITH_EMAIL, USER_DOES_NOT_EXIST
}
