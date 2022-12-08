package com.sandf.security

import com.sandf.api.model.Authority
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import lombok.RequiredArgsConstructor
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*

@Component
@RequiredArgsConstructor
class JwtComposer {

    @Value("\${security.secretKey}")
    private val secretKey: String? = null

    private val logger = KotlinLogging.logger {}

    fun createJwtToken(email: String, authorities: Collection<Authority>): String {
        return try {
            val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

            Jwts.builder()
                .setSubject(email)
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(1).toInstant()))
                .addClaims(mapOf("authorities" to extractAuthoritiesToStrings(authorities)))
                .signWith(key)
                .compact()
        } catch (exception: Exception) {
            logger.error(exception) { "Cannot compose JWT token {$exception}" }
            throw RuntimeException(exception)
        }
    }

    private fun extractAuthoritiesToStrings(authorities: Collection<Authority>): Set<String> {
        return authorities.map { obj -> obj.name }.toSet()
    }

}
