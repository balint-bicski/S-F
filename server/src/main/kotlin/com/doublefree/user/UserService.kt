package com.doublefree.user

import com.doublefree.util.UserUtil
import com.doublefree.validation.ValidationErrorCode
import com.doublefree.validation.ValidationExceptionDto
import lombok.RequiredArgsConstructor
import mu.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
@RequiredArgsConstructor
class UserService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) {

    private val logger = KotlinLogging.logger {}

    fun currentUser(): UserDto {
        val emailOfLoggedInUser = UserUtil.emailOfLoggedInUser()
        return getOne(emailOfLoggedInUser)
            .orElseThrow { NoSuchElementException("No user found with email: $emailOfLoggedInUser") }
            .toDto()
    }

    fun getOne(id: Long) = userRepository.findById(id)
        .orElseThrow { NoSuchElementException("No user found with id: $id") }
        .toDto()

    fun create(userDto: UserDto): Long {
        validateUser(userDto)
        encodeUserPassword(userDto)
        val savedUser = userRepository.save(userDto.toEntity())
        logger.info { "User has been created {${userDto.copy(id = savedUser.id)}}" }
        return requireNotNull(savedUser.id)
    }

    private fun getOne(email: String) = userRepository.findByEmail(email)

    private fun validateUser(userDto: UserDto) {
        if (getOne(userDto.email).isPresent) {
            throw ValidationExceptionDto.ofError(
                ValidationErrorCode.USER_EXIST_WITH_EMAIL,
                mapOf("email" to userDto.email)
            )
        }
    }

    private fun encodeUserPassword(userDto: UserDto) {
        userDto.password = passwordEncoder.encode(userDto.password)
    }

}
