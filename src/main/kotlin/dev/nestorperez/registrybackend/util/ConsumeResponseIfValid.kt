package dev.nestorperez.registrybackend.util

import retrofit2.Response

inline fun <T, R> Response<T>.consumeResponseIfValid(consumer: (body: T) -> R): R? {
    return if (this.isSuccessful && this.body() != null) {
        consumer(this.body()!!)
    } else {
        null
    }
}