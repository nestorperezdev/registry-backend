package dev.nestorperez.registrybackend.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CypherAuthSettingsTest {
    @Test
    fun `authSettings should decipher settings`() {
        val cypherAuthSettings = CypherAuthSettings(token = "")
        assertEquals(AuthSettings(username = "", password = ""), cypherAuthSettings.authSettings)
    }
}