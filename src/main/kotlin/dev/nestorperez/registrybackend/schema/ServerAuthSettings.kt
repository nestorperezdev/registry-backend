package dev.nestorperez.registrybackend.schema

import dev.nestorperez.registrybackend.util.decipherAuthSettings


data class AuthSettings(val username: String, val password: String)

data class CypherAuthSettings(val token: String) {
    val authSettings: AuthSettings by lazy { decipherAuthSettings(token) }
}
