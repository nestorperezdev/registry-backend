package dev.nestorperez.registrybackend.registry

import dev.nestorperez.registrybackend.model.Context
import io.mockk.every
import io.mockk.mockk
import okhttp3.Request
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ContextRegistryTest {

    @Test
    fun `should find context on given tag`() {
        val tagContext: Context = mockk()
        val request = Request.Builder()
            .tag(Context::class.java, tagContext)
            .url("http://localhost.com")
            .build()
        val result = findContextOnRequest(request)
        assertEquals(result, tagContext)
    }
}