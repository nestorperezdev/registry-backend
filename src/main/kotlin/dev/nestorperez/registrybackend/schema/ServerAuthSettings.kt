package dev.nestorperez.registrybackend.schema

import dev.nestorperez.registrybackend.util.decipherAuthSettings


data class AuthSettings(
    val basicAuth: BasicAuth? = null,
    val jwtToken: JwtToken? = null,
    val dockerIoAuthSettings: DockerIoAuthSettings? = null
)

data class BasicAuth(val username: String, val password: String)

data class JwtToken(val token: String)

data class DockerIoAuthSettings(
    val tokenUrl: String = "https://auth.docker.io/token",
    val service: String = "registry.docker.io",
    val scope: String = "repository:library/alpine:pull"
)

data class CypherAuthSettings(val token: String) {
    val authSettings: AuthSettings by lazy { decipherAuthSettings(token) }
}

