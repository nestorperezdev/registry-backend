package dev.nestorperez.registrybackend.registry.interceptors

import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import org.junit.jupiter.api.Test
import okhttp3.Response

import org.junit.jupiter.api.Assertions.*

class ErrorHandlingInterceptorTest {

    val sut: ErrorHandlingInterceptor = ErrorHandlingInterceptor()

    @Test
    fun intercept() {
        val errorResponse = Response.Builder()
            .request(Request.Builder().url("http://localhost").build())
            .protocol(Protocol.HTTP_1_1)
            .code(500)
            .message("Different server message")
            .build()
        val chain: Interceptor.Chain = mockk {
            every { proceed(any()) }.returns(errorResponse)
            every { request() }.returns(Request.Builder().url("http://localhost").build())
        }
        val response = sut.intercept(chain)
        assertEquals("Internal server error", response.message)
    }

    @Test
    fun `should left intact the response if response is succesful`() {
        val successResponse = Response.Builder()
            .request(Request.Builder().url("http://localhost").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("Different server message")
            .build()
        val chain: Interceptor.Chain = mockk {
            every { proceed(any()) }.returns(successResponse)
            every { request() }.returns(Request.Builder().url("http://localhost").build())
        }
        val response = sut.intercept(chain)
        assertEquals("Different server message", response.message)
    }

    /**
     * Test every possible error code
     */
    @Test
    fun handleError() {
        var errorResponse = Response.Builder()
            .code(400)
            .message("Bad request")
            .request(Request.Builder().url("http://localhost").build())
            .protocol(Protocol.HTTP_1_1)
            .build()
        assertEquals("Bad request", errorResponse.handleError())
        errorResponse = errorResponse.newBuilder().code(401).message("Unauthorized").build()
        assertEquals("Unauthorized", errorResponse.handleError())
        errorResponse = errorResponse.newBuilder().code(403).message("Forbidden").build()
        assertEquals("Forbidden", errorResponse.handleError())
        errorResponse = errorResponse.newBuilder().code(404).message("Not found").build()
        assertEquals("Not found", errorResponse.handleError())
        errorResponse = errorResponse.newBuilder().code(500).message("Internal server error").build()
        assertEquals("Internal server error", errorResponse.handleError())
        errorResponse = errorResponse.newBuilder().code(200).message("Success").build()
        assertEquals("Success", errorResponse.handleError())
        errorResponse = errorResponse.newBuilder().code(999).message("Unknown error").build()
        assertEquals("Unknown error", errorResponse.handleError())
    }
}