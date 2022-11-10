package com.doublefree.authentication

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class IdResponseDto(@JsonProperty("id") val id: Long?) : Serializable {
    data class Builder(var id: Long? = null) {
        fun id(id: Long) = apply { this.id = id }
        fun build() = IdResponseDto(id)
    }
}
