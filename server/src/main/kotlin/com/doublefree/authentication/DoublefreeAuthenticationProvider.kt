package com.doublefree.authentication

import com.doublefree.user.UserRepository
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class PtmaAuthenticationProvider(
    private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val email = authentication.principal.toString()
        val user = userRepository.findByEmail(email)
        require(user.isPresent) { throw BadCredentialsException("Invalid username or password!") }

        val password = authentication.credentials.toString()
        if (!passwordEncoder.matches(password, user.get().password)) {
            throw BadCredentialsException("Invalid username or password!")
        }
        return UsernamePasswordAuthenticationToken(email, password, user.get().authorities.toGrantedAuthorities())
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}
