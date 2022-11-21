package com.bloobloom.assessment.model.repository

import com.bloobloom.assessment.model.Frame
import com.bloobloom.assessment.model.Status
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FrameRepository : MongoRepository<Frame, String> {
    fun findAllByStatus(status: Status): MutableList<Frame>
}

