package com.sandf.authentication

import com.sandf.api.AuthenticationApi
import com.sandf.api.model.AuthResultDto
import com.sandf.api.model.IdResponseDto
import com.sandf.api.model.UserCredentialsDto
import com.sandf.api.model.UserDto
import com.sandf.user.UserService
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
