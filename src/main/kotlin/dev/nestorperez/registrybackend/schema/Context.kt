package dev.nestorperez.registrybackend.schema

data class Context(
    val url: String,
    val port: Int?,
    val authSettings: AuthSettings?
) {
}
