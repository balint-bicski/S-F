package com.doublefree.authentication

import com.doublefree.user.UserDto
import com.doublefree.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class AuthenticationController(
    private val authenticationService: AuthenticationService,
    private val userService: UserService
) {

    /**
     * POST /api/auth/login : Login
     *
     * @param userCredentialsDto User credentials (required)
     * @return Operation successful (status code 200)
     */
    @RequestMapping(
        value = ["/api/auth/login"],
        produces = ["application/json"],
        consumes = ["application/json"],
        method = [RequestMethod.POST]
    )
    fun login(@RequestBody userCredentialsDto: UserCredentialsDto): ResponseEntity<AuthResultDto> {
        return ResponseEntity.ok(
            AuthResultDto.Builder()
                .jwtToken(authenticationService.authenticate(userCredentialsDto))
                .build()
        )
    }

    /**
     * POST /api/auth/register : Register a user
     *
     * @param userDto UserDto (required)
     * @return The new entity is created (status code 201)
     *         or Bad Request (status code 422)
     */
    @RequestMapping(
        value = ["/api/auth/register"],
        produces = ["application/json"],
        consumes = ["application/json"],
        method = [RequestMethod.POST]
    )
    fun register(@RequestBody userDto: UserDto) = ResponseEntity.ok(
        IdResponseDto.Builder()
            .id(userService.create(userDto))
            .build()
    )
}
