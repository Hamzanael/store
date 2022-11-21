package com.bloobloom.assessment.config

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.cache.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeansConfiguration {
    @Bean
    fun httpClient() = HttpClient(OkHttp) {
        expectSuccess = true
        install(HttpCache)
    }
}