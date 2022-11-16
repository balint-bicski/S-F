package com.doublefree.user

import com.doublefree.api.UserApi
import com.doublefree.api.model.UserDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) : UserApi {

    override fun getCurrentUser(): ResponseEntity<UserDto> = ResponseEntity.ok(userService.currentUser())

}
