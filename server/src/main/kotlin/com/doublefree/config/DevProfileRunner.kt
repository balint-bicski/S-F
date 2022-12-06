package com.doublefree.config

import com.doublefree.authentication.Role
import com.doublefree.user.User
import com.doublefree.user.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Profile("dev")
@Service
class DevProfileRunner(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        userRepository.save(User(
            id = 32768,
            email = "admin@user.com",
            password = passwordEncoder.encode("root"),
            bio = "",
            role = Role.ADMINISTRATOR
        ))
    }
}
