package com.sandf.security

import lombok.RequiredArgsConstructor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter(private val jwtParser: JwtParser) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token = getToken(request)
        if (token == null) {
            logger.warn("Token cannot be obtained from request")
            chain.doFilter(request, response)
            return
        }
        try {
            val auth = jwtParser.parseJwt(token)
            SecurityContextHolder.getContext().authentication = auth
        } catch (e: Exception) {
            logger.error("Failed to parse JWT token", e)
            SecurityContextHolder.clearContext()
        }
        chain.doFilter(request, response)
    }

    private fun getToken(request: HttpServletRequest): String? {
        return Optional.ofNullable(request.getHeader("Authorization"))
            .map { auth -> auth.replace("Bearer", "") }
            .orElse(null)
    }

}
