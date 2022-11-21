package com.bloobloom.assessment.api.v1.controller.user

import com.bloobloom.assessment.model.Lenses
import com.bloobloom.assessment.security.authorizedUser
import com.bloobloom.assessment.service.LensesService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RequestMapping("api/v1/user/lenses")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ROLE_USER')")
@RestController
class UserLensesController {
    @Autowired
    private lateinit var lensesService: LensesService

    @GetMapping
    fun getAllLenses(): ResponseEntity<List<Lenses>> {
        logger.info("Display all lenses for user")
        val currency = authorizedUser().currency
        return ResponseEntity.ok(lensesService.getLenses(
            currency
        ))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserLensesController::class.java)
    }
}