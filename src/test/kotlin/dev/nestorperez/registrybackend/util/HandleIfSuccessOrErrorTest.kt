package dev.nestorperez.registrybackend.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.Response

class HandleIfSuccessOrErrorTest {
    @Test
    fun `handleIfSuccessOrError should return success`() {
        val response = Response.success("success")
        val result = response.handleIfSuccessOrError(
            success = { it },
            error = { "error" }
        )
        assertEquals("success", result)
    }

    @Test
    fun `should handleIfSuccessOrError return error`() {
        val rawResponse: okhttp3.Response = okhttp3.Response.Builder()
            .code(400)
            .message("my error")
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .request(okhttp3.Request.Builder().url("http://localhost").build())
            .body("error".toResponseBody("application/json".toMediaTypeOrNull()))
            .build()
        val response = Response.error<String>(rawResponse.body!!, rawResponse)
        val result = response.handleIfSuccessOrError(
            success = { "success" },
            error = { it }
        )
        assertEquals("my error", result)
    }

    @Test
    fun `should handleIfSuccessOrError if body is null`() {
        val response = Response.success(null)
        val result = response.handleIfSuccessOrError(
            success = { it },
            error = { it }
        )
        assertEquals("Error fetching data", result)
    }
}