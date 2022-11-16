package com.doublefree.authentication

import com.doublefree.api.AuthenticationApi
import com.doublefree.api.model.AuthResultDto
import com.doublefree.api.model.IdResponseDto
import com.doublefree.api.model.UserCredentialsDto
import com.doublefree.api.model.UserDto
import com.doublefree.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class AuthenticationController(
    private val authenticationService: AuthenticationService, private val userService: UserService
) : AuthenticationApi {

    override fun login(userCredentialsDto: UserCredentialsDto): ResponseEntity<AuthResultDto> = ResponseEntity.ok(
        AuthResultDto(jwtToken = authenticationService.authenticate(userCredentialsDto))
    )

    override fun register(userDto: UserDto): ResponseEntity<IdResponseDto> = ResponseEntity.ok(
        IdResponseDto(id = userService.create(userDto))
    )

}
