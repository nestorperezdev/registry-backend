package dev.nestorperez.registrybackend.util

import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

fun <T> buildApiResponse(
    isSuccessful: Boolean = false,
    body: T? = null,
    responseCode: Int = 400,
    errorMessage: String = "Default error",
    extraHeaders: Map<String, String> = mapOf()
): Response<T> {
    if (isSuccessful) {
        return Response.success(body, extraHeaders.toHeaders())
    } else {
        return Response.error(responseCode, errorMessage.toResponseBody("text/plain".toMediaTypeOrNull()))
    }
}
