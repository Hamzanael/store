package com.bloobloom.assessment.api.v1.controller.user

import com.bloobloom.assessment.model.ItemPurchase
import com.bloobloom.assessment.security.authorizedUser
import com.bloobloom.assessment.service.ItemPurchaseService
import com.bloobloom.assessment.service.UserService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/v1/user/check-out")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ROLE_USER')")
@RestController
class PurchaseController {

    @Autowired
    private lateinit var purchaseService: ItemPurchaseService

    @Autowired
    private lateinit var userService: UserService

    @PostMapping
    fun checkout(): ResponseEntity<ItemPurchase> = ResponseEntity.ok(
        purchaseService.purchaseItem(
            getCurrentUserIfExists()
        )
    )

    private fun getCurrentUserIfExists() =
        userService.getUserByEmail(authorizedUser().username) ?: throw UsernameNotFoundException("User not found")

}