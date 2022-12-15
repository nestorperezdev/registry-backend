package dev.nestorperez.registrybackend.util

import dev.nestorperez.registrybackend.model.AuthSettings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DecipherAuthSettingsTest {
    @Test
    fun `should decipher AuthSettings`() {
        val cipherStr = ""
        val expected = AuthSettings(username = "", password = "")
        val result = decipherAuthSettings(cipherStr)
        assertEquals(result, expected)
    }
}