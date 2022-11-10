package com.doublefree.authentication

import com.doublefree.security.JwtComposer
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@Service
@Transactional
class AuthenticationService(
    private val request: HttpServletRequest,
    private val authenticationManager: AuthenticationManager,
    private val jwtComposer: JwtComposer
) {

    private val logger = KotlinLogging.logger {}

    fun authenticate(userCredentialsDto: UserCredentialsDto): String {
        val email = userCredentialsDto.email.lowercase()
        val authRequest = UsernamePasswordAuthenticationToken(email, userCredentialsDto.password).also {
            it.details = WebAuthenticationDetailsSource().buildDetails(request)
        }
        val authResult = authenticationManager.authenticate(authRequest)
        logger.info { "User has logged in with email: $email" }
        return jwtComposer.createJwtToken(email, authResult.authorities)
    }

}
