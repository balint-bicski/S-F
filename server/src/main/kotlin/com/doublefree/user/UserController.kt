package com.doublefree.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {

    /**
     * GET /api/current-user
     *
     * @return Current user (status code 200)
     *         or Not Found (status code 404)
     */
    @RequestMapping(value = ["/api/current-user"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getCurrentUserInfo() = ResponseEntity.ok(userService.currentUser())

}
