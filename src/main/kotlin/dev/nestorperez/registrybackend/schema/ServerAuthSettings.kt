package dev.nestorperez.registrybackend.schema

import dev.nestorperez.registrybackend.util.decipherAuthSettings


data class AuthSettings(
    val basicAuth: BasicAuth? = null,
    val jwtToken: JwtToken? = null
)

data class BasicAuth(val username: String, val password: String)

data class JwtToken(val token: String)

data class CypherAuthSettings(val token: String) {
    val authSettings: AuthSettings by lazy { decipherAuthSettings(token) }
}

