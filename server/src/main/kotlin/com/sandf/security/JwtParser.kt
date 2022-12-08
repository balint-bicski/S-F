package com.sandf.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
@RequiredArgsConstructor
class JwtParser {

    @Value("\${security.secretKey}")
    private val secretKey: String? = null

    fun parseJwt(jwt: String?): Authentication {
        val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .body

        val grantedAuthorities = (claims["authorities"] as List<*>)
            .map { role -> SimpleGrantedAuthority(role as String) }
            .toList()

        val userDetails: UserDetails = User(claims.subject, "", grantedAuthorities)

        return UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities)
    }

}
