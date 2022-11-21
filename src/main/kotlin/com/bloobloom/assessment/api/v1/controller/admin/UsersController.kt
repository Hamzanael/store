package com.bloobloom.assessment.api.v1.controller.admin

import com.bloobloom.assessment.api.v1.request.UserRequest
import com.bloobloom.assessment.api.v1.response.UserResponse
import com.bloobloom.assessment.api.v1.response.toResponse
import com.bloobloom.assessment.api.v1.response.toUser
import com.bloobloom.assessment.model.User
import com.bloobloom.assessment.service.UserService
import com.bloobloom.assessment.service.exceptions.NoEntityFoundException
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@SecurityRequirement(name = "JWT")
@CrossOrigin(origins = ["http://localhost:3000"])
class UsersController {
    @Autowired
    lateinit var userService: UserService

    @GetMapping
    fun getAllUsers(): List<UserResponse> {
        log.info("Display all users")
        return userService.getAll().map { it.toResponse() }
    }

    @PostMapping
    fun registerUser(
        @RequestBody user: User
    ): ResponseEntity<UserResponse> {
        log.info("Register new user")
        val registeredUser = userService.create(user)
        return ResponseEntity(registeredUser.toResponse(), CREATED)
    }

    @DeleteMapping("{id}")
    fun deleteUser(@PathVariable("id") id: String) {
        try {
            log.info("delete an existing user with id: $id")
            userService.delete(id)
        } catch (exception: NoEntityFoundException) {
            log.error("Exception: account with id $id not exists to delete")
            throw NoEntityFoundException("account with id $id not exists to delete")
        }
    }

    @PutMapping("{id}")
    fun updateUser(
        @PathVariable id: String,
        @RequestBody user: UserRequest
    ): ResponseEntity<UserResponse> {
        log.info("update information for an existing user with id $id")
        val newUser = userService.update(
            user.toUser().copy(password = userService.getUserByEmail(user.email)!!.password),
            id
        )
        return ResponseEntity(newUser.toResponse(), ACCEPTED)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UsersController::class.java)
    }
}
