package com.sandf.user

import com.sandf.api.model.UserDto
import com.sandf.util.UserUtil
import com.sandf.validation.ValidationErrorCode
import com.sandf.validation.ValidationExceptionDto
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
        return findByEmail(emailOfLoggedInUser!!).orElseThrow { NoSuchElementException("No user found with email: $emailOfLoggedInUser") }
            .toDto()
    }

    fun create(userDto: UserDto): Long {
        validateUserExistByEmail(userDto.email)
        val userWithEncodedPassword = encodeUserPassword(userDto)
        val savedUser = userRepository.save(userWithEncodedPassword.toEntity())
        logger.info { "User has been created {${userDto.copy(id = savedUser.id)}}" }
        return requireNotNull(savedUser.id)
    }

    fun getUsers(): List<UserDto> = userRepository.findAll().map { it.toDto() }

    fun delete(id: Long) {
        validateUserNotExistById(id)
        userRepository.deleteById(id)
    }

    private fun findByEmail(email: String) = userRepository.findByEmail(email)

    private fun validateUserExistByEmail(email: String) {
        if (findByEmail(email).isPresent) {
            throw ValidationExceptionDto.ofError(
                ValidationErrorCode.USER_EXIST_WITH_EMAIL, mapOf("email" to email)
            )
        }
    }

    private fun validateUserNotExistById(userId: Long) {
        if (!userRepository.existsById(userId)) {
            throw ValidationExceptionDto.ofError(
                ValidationErrorCode.USER_DOES_NOT_EXIST, mapOf("id" to userId.toString())
            )
        }
    }

    private fun encodeUserPassword(userDto: UserDto): UserDto {
        return userDto.copy(password = passwordEncoder.encode(userDto.password))
    }

}
