package dev.nestorperez.registrybackend.registry.model

import com.google.gson.annotations.SerializedName

data class DockerIoRegistryToken(
    @SerializedName("token") val token: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("issued_at") val issuedAt: String
)
