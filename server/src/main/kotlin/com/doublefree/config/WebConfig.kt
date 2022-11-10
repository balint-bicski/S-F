package com.doublefree.config

import com.doublefree.security.JwtAuthenticationFilter
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebConfig(private val jwtAuthenticationFilter: JwtAuthenticationFilter) : WebMvcConfigurer {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun webSecurityCustomizer() =
        WebSecurityCustomizer { web -> web.ignoring().antMatchers("/", "/**/*.{js,html,ico}") }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        sessionCreationPolicy(http) // no sessions because JWT is stateless
        jwtAuthFilter(http) // using JWT authentication
        entryPoint(http) // suppress auth form
        csrf(http) // no sessions, so no cross-site request forgery protection is necessary
        cors(http) // using CorsConfigurer for cross origin resource sharing
        authorizeRequests(http) // authorize requests
        return http.build()
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedMethods("*")
            .allowedOrigins("http://localhost:4200")
            .allowedHeaders("*")
            .allowCredentials(true)
    }

    @Throws(Exception::class)
    private fun sessionCreationPolicy(http: HttpSecurity) {
        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    private fun entryPoint(http: HttpSecurity) {
        http.exceptionHandling().authenticationEntryPoint(RestAuthenticationEntryPoint())
    }

    @Throws(Exception::class)
    private fun authorizeRequests(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .anyRequest().fullyAuthenticated()
    }

    @Throws(Exception::class)
    private fun cors(http: HttpSecurity) {
        http.cors()
    }

    private fun jwtAuthFilter(http: HttpSecurity) {
        http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Throws(Exception::class)
    private fun csrf(http: HttpSecurity) {
        http.csrf().disable()
    }
}
