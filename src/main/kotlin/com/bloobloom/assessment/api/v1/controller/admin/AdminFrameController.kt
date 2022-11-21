package com.bloobloom.assessment.api.v1.controller.admin

import com.bloobloom.assessment.api.v1.request.UpdateFrameRequest
import com.bloobloom.assessment.model.Frame
import com.bloobloom.assessment.service.FrameService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/admin/frames")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
class AdminFrameController {
    @Autowired
    private lateinit var frameService: FrameService

    @GetMapping
    fun getAllFrames(): ResponseEntity<List<Frame>> {
        logger.info("Display all frames")
        return ResponseEntity.ok(frameService.getFrames())
    }

    @PostMapping
    fun createFrame(@RequestBody frame: Frame): ResponseEntity<Frame> {
        logger.info("Create new frame")
        return ResponseEntity.ok(frameService.create(frame))
    }

    @PatchMapping("{id}")
    fun updateFrame(@PathVariable id: String, @RequestBody frame: UpdateFrameRequest): ResponseEntity<Frame> {
        logger.info("Update frame with id: $id")
        return ResponseEntity.ok(frameService.update(id, frame))
    }

    @PatchMapping("{id}/stock")
    fun addFrameToStock(@PathVariable id: String, @RequestParam quantity: Int): ResponseEntity<Frame> {
        logger.info("Add $quantity frames to stock with id: $id")
        return ResponseEntity(frameService.addFrameToStock(id, quantity), HttpStatus.ACCEPTED)
    }

    @DeleteMapping("{id}")
    fun deleteFrame(@PathVariable id: String): ResponseEntity<Any> {
        logger.info("Delete frame with id: $id")
        frameService.delete(id)
        return ResponseEntity.noContent().build()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AdminFrameController::class.java)
    }
}