package com.bloobloom.assessment.api.v1.controller.user

import com.bloobloom.assessment.api.v1.request.ShoppingBasketRequest
import com.bloobloom.assessment.model.ShoppingBasket
import com.bloobloom.assessment.security.authorizedUser
import com.bloobloom.assessment.service.ShoppingBasketService
import com.bloobloom.assessment.service.UserService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/user/shopping-basket")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ROLE_USER')")
@RestController
class ShoppingBasketController {

    @Autowired
    private lateinit var shoppingBasketService: ShoppingBasketService

    @Autowired
    private lateinit var userService: UserService

    @GetMapping
    fun getShoppingBasket(): ResponseEntity<ShoppingBasket> {
        val user =
            getCurrentUserIfExists()
        return ResponseEntity.ok(shoppingBasketService.getShoppingBasket(user))
    }

    @PostMapping
    fun addShoppingBasket(@RequestBody shoppingBasketRequest: ShoppingBasketRequest): ResponseEntity<ShoppingBasket> =
        ResponseEntity.ok(
            shoppingBasketService.addToShoppingBasket(
                getCurrentUserIfExists(),
                shoppingBasketRequest.lensesIdAndQuantity,
                shoppingBasketRequest.frameIdAndQuantity
            )
        )
    

    private fun getCurrentUserIfExists() =
        userService.getUserByEmail(authorizedUser().username) ?: throw UsernameNotFoundException("User not found")
}