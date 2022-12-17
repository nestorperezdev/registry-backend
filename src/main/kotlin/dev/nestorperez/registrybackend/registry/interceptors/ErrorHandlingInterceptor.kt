package dev.nestorperez.registrybackend.registry.interceptors

import graphql.VisibleForTesting
import okhttp3.Interceptor
import okhttp3.Response

class ErrorHandlingInterceptor : Interceptor {
    /**
     * Intercept retrofit call and throw exception if response is not successful
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (!response.isSuccessful) {
            return response
                .newBuilder()
                .message(response.handleError())
                .build()
        }
        return response
    }

}

/**
 * Handle the error returned by the response and return a friendly message to the user
 */
@VisibleForTesting
fun Response.handleError(): String {
    return when (this.code) {
        200 -> "Success"
        400 -> "Bad request"
        401 -> "Unauthorized"
        403 -> "Forbidden"
        404 -> "Not found"
        500 -> "Internal server error"
        else -> "Unknown error"
    }
}