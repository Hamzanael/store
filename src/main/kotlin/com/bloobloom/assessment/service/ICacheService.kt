package com.bloobloom.assessment.service

interface ICacheService<T> {
    fun get(key: String): T?
    fun set(key: String, value: T)
}