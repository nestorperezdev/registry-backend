package dev.nestorperez.registrybackend.model

data class Context(
    val url: String,
    val port: Int?,
    val authSettings: AuthSettings?
)
