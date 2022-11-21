package com.bloobloom.assessment.api.v1.controller.admin

import com.bloobloom.assessment.api.v1.request.UpdateLensesRequest
import com.bloobloom.assessment.model.Lenses
import com.bloobloom.assessment.service.LensesService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RequestMapping("api/v1/lenses")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
class AdminLensesController {
    @Autowired
    private lateinit var lensesService: LensesService

    @GetMapping
    fun getAllLenses(): ResponseEntity<List<Lenses>> {
        logger.info("Display all lenses for admin")
        return ResponseEntity.ok(lensesService.getLenses())
    }

    @PostMapping
    fun createLenses(@RequestBody lenses: Lenses): ResponseEntity<Lenses> {
        logger.info("Create new lenses")
        return ResponseEntity.ok(lensesService.create(lenses))
    }

    @PatchMapping("{id}")
    fun updateLenses(@PathVariable id: String, @RequestBody lenses: UpdateLensesRequest): ResponseEntity<Lenses> {
        logger.info("Update lenses with id: $id")
        return ResponseEntity.ok(lensesService.update(id, lenses))
    }

    @PatchMapping("{id}/stock")
    fun addLensesToStock(@PathVariable id: String, @RequestParam quantity: Int): ResponseEntity<Lenses> {
        logger.info("Add lenses with id: $id to stock")
        return ResponseEntity.ok(lensesService.addLensesToStock(id, quantity))
    }


    @DeleteMapping("{id}")
    fun deleteLenses(@PathVariable id: String): ResponseEntity<Any> {
        logger.info("Delete lenses with id: $id")
        lensesService.delete(id)
        return ResponseEntity.noContent().build()
    }


    companion object {
        private val logger = LoggerFactory.getLogger(AdminLensesController::class.java)
    }
}