package com.doublefree.user

import com.doublefree.authentication.Role
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "auth_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,
    @Column(nullable = false)
    val email: String,
    @Column(nullable = false)
    val password: String,
) {
    val authorities get() = role.authorities()

    fun toDto() = UserDto(id, role, email)
}

data class UserDto(
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("role") val role: Role,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") var password: String? = null
) : Serializable {
    fun toEntity() = User(id, role, email, password = requireNotNull(password))
}
