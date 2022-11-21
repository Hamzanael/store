package com.bloobloom.assessment.api.v1.controller.user

import com.bloobloom.assessment.model.Frame
import com.bloobloom.assessment.model.Status.ACTIVE
import com.bloobloom.assessment.service.FrameService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/user/frames")
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasRole('ROLE_USER')")
@RestController
class UserFrameController {
    @Autowired
    private lateinit var frameService: FrameService

    @GetMapping
    fun getAllFrames(): ResponseEntity<List<Frame>> {
        logger.info("Display all frames for user")
        return ResponseEntity.ok(frameService.getFramesByStatus(ACTIVE))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserFrameController::class.java)
    }
}