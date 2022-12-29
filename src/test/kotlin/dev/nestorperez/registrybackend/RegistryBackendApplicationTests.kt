package dev.nestorperez.registrybackend

import io.mockk.every
import io.mockk.mockk
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment

@SpringBootTest
class RegistryBackendApplicationTests {
    val environment: Environment = mockk()

    @Test
    fun `should enable basic logging`() {
        every { environment.activeProfiles } returns arrayOf("prod")
        val result = loggingInterceptor(environment)
        assert(result.level == HttpLoggingInterceptor.Level.BASIC)
    }

    @Test
    fun `should enable body logging`() {
        every { environment.activeProfiles } returns arrayOf("dev")
        val result = loggingInterceptor(environment)
        assert(result.level == HttpLoggingInterceptor.Level.BODY)
    }
}
