package com.doublefree.authentication

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class AuthResultDto(@JsonProperty("jwtToken") val jwtToken: String? = null) : Serializable {
    data class Builder(var jwtToken: String? = null) {
        fun jwtToken(jwtToken: String) = apply { this.jwtToken = jwtToken }
        fun build() = AuthResultDto(jwtToken)
    }
}
