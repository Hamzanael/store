package com.bloobloom.assessment.service

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CacheService<T>(
    @Value("\${config.caching.expireTime}")
    val cacheExpireTime: Long? = null
) : ICacheService<T> {
    private val data: Cache<String, T> = CacheBuilder.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(cacheExpireTime!!))
        .concurrencyLevel(Runtime.getRuntime().availableProcessors())
        .build();

    override fun get(key: String): T? {
        return data.getIfPresent(key)
    }

    override fun set(key: String, value: T) {
        data.put(key, value)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CacheService::class.java)
    }
}