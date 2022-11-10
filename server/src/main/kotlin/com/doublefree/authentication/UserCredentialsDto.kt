package com.doublefree.authentication

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class UserCredentialsDto(
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String
) : Serializable
