package dev.nestorperez.registrybackend.util

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConsumeResponseIfValidTest {
    @Test
    fun `should consume response if body is present and is success`() {
        val body = mockk<Any>()
        val response = buildApiResponse(isSuccessful = true, body = body)
        assertEquals(response.consumeResponseIfValid { it }, body)
    }

    @Test
    fun `should NOT consume response if body null`() {
        val response = buildApiResponse<Any>(isSuccessful = true, body = null)
        assertNull(response.consumeResponseIfValid { it } ?: kotlin.run { null })
    }

    @Test
    fun `should NOT consume response if response is failed`() {
        val response = buildApiResponse<Any>(isSuccessful = false)
        assertNull(response.consumeResponseIfValid { it } ?: kotlin.run { null })
    }
}