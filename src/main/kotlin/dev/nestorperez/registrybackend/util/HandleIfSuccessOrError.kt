package dev.nestorperez.registrybackend.util

import retrofit2.Response

inline fun <T, R> Response<T>.handleIfSuccessOrError(success: (body: T) -> R, error: (message: String) -> R): R =
    if (this.isSuccessful && this.body() != null) {
        success(this.body()!!)
    } else if (this.isSuccessful && this.body() == null) {
        error("Error fetching data")
    } else {
        error(this.message())
    }