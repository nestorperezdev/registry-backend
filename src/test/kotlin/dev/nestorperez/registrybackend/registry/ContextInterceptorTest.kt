package dev.nestorperez.registrybackend.registry

import dev.nestorperez.registrybackend.model.AuthSettings
import dev.nestorperez.registrybackend.model.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ContextInterceptorTest {
    @Test
    fun `should intercept chain request but not change url nor auth`() {
        val response: Response = mockk()
        val request = Request.Builder()
            .url("http://localhost/v2/catalog/otherpath")
            .build()
        val chain: Interceptor.Chain = mockk {
            every { request() }.returns(request)
            every { proceed(any()) }.returns(response)
        }
        val result = ContextInterceptor().intercept(chain)
        verify(exactly = 1) { chain.request() }
        verify(exactly = 1) { chain.proceed(any()) }
        assertEquals(response, result)
        verify {
            chain.proceed(withArg {
                assertEquals("http://localhost/v2/catalog/otherpath", it.url.toString())
                assertNull(it.header("Authorization"))
            })
        }
    }

    @Test
    fun `should intercept chain request and assign new url`() {
        val response: Response = mockk()
        val context = Context(url = "http://otherhost", port = null, authSettings = null)
        val request = Request.Builder()
            .url("http://localhost/v2/catalog/otherpath")
            .tag(Context::class.java, context)
            .build()
        val chain: Interceptor.Chain = mockk {
            every { request() }.returns(request)
            every { proceed(any()) }.returns(response)
        }
        val result = ContextInterceptor().intercept(chain)
        verify(exactly = 1) { chain.request() }
        verify(exactly = 1) { chain.proceed(any()) }
        assertEquals(response, result)
        verify {
            chain.proceed(withArg {
                assertEquals("http://otherhost/v2/catalog/otherpath", it.url.toString())
                assertNull(it.header("Authorization"))
            })
        }
    }

    @Test
    fun `should intercept chain request and assign new url and authorization`() {
        val response: Response = mockk()
        val context = Context(
            url = "http://otherhost",
            port = null,
            authSettings = AuthSettings(username = "username", password = "password")
        )
        val request = Request.Builder()
            .url("http://localhost/v2/catalog/otherpath")
            .tag(Context::class.java, context)
            .build()
        val chain: Interceptor.Chain = mockk {
            every { request() }.returns(request)
            every { proceed(any()) }.returns(response)
        }
        val result = ContextInterceptor().intercept(chain)
        verify(exactly = 1) { chain.request() }
        verify(exactly = 1) { chain.proceed(any()) }
        assertEquals(response, result)
        verify {
            chain.proceed(withArg {
                assertEquals("http://otherhost/v2/catalog/otherpath", it.url.toString())
                assertNotNull(it.header("Authorization"))
                assertEquals(
                    Credentials.basic(context.authSettings!!.username, context.authSettings!!.password),
                    it.header("Authorization")
                )
            })
        }
    }
}