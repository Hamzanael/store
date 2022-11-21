package com.bloobloom.assessment.model.repository

import com.bloobloom.assessment.model.Frame
import com.bloobloom.assessment.model.Lenses
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LensesRepository : MongoRepository<Lenses, String>

