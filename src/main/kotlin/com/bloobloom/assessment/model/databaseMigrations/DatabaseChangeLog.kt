package com.bloobloom.assessment.model.databaseMigrations

import com.bloobloom.assessment.model.Currency.JOD
import com.bloobloom.assessment.model.User
import com.bloobloom.assessment.model.enums.Role.ADMIN
import com.bloobloom.assessment.model.enums.Role.USER
import com.bloobloom.assessment.model.repository.UserRepository
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution

@ChangeUnit(id = "client-initializer", order = "1", author = "Hamza")
class DatabaseChangeLog {
    @Execution
    fun execution(
        userRepository: UserRepository,
    ) {
        userRepository.save(
            User(
                "Admin",
                "admin@store.com",
                "\$2a\$06\$wXeT6.fMGbgrSLPaxndWte9JgQsRYj36RIyZ4MHw3/MeSMuaSnEiq", // password: 123456789#
                ADMIN,
            )
        )
        userRepository.save(
            User(
                "user",
                "user@store.com",
                "\$2a\$06\$wXeT6.fMGbgrSLPaxndWte9JgQsRYj36RIyZ4MHw3/MeSMuaSnEiq", // password: 123456789#
                role = USER,
                currency = JOD
            )
        )
    }

    @RollbackExecution
    fun rollbackExecution(
        userRepository: UserRepository,
    ) {
        userRepository.deleteAll()
    }
}
