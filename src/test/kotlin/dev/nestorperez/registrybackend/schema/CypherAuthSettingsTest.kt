package dev.nestorperez.registrybackend.schema

import dev.nestorperez.registrybackend.schema.AuthSettings
import dev.nestorperez.registrybackend.schema.CypherAuthSettings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CypherAuthSettingsTest {
    @Test
    fun `authSettings should decipher settings`() {
        val cypherAuthSettings = CypherAuthSettings(token = "")
        assertEquals(AuthSettings(), cypherAuthSettings.authSettings)
    }
}