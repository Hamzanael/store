package com.bloobloom.assessment

import io.mongock.runner.springboot.EnableMongock
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@EnableMongock
@OpenAPIDefinition(
    info = Info(
        title = "bloobloom Assessment API",
        version = "1.0",
        description = "bloobloom Assessment API "
    )
)
@SecurityScheme(
    name = "JWT",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT"
)
@SpringBootApplication
class AssessmentApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder().sources(AssessmentApplication::class.java).run(*args)
}
