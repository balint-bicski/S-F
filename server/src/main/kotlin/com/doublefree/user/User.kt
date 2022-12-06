package com.doublefree.user

import com.doublefree.api.model.UserDto
import com.doublefree.authentication.Role
import javax.persistence.*

@Entity
@Table(name = "auth_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val id: Long? = null,
    @Column(nullable = false)
    val email: String,
    @Column(nullable = false)
    val password: String,
    @Column(nullable = true)
    val bio: String?,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.USER,
) {
    val authorities get() = role.authorities()

    fun toDto() = UserDto(id = id, email = email, bio = bio, role = role.name)
}

fun UserDto.toEntity() = User(id, email, requireNotNull(password), bio)
