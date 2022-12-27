package dev.nestorperez.registrybackend.authapi

import dev.nestorperez.registrybackend.registry.model.DockerIoRegistryToken
import dev.nestorperez.registrybackend.schema.DockerIoAuthSettings
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Tag

interface AuthApi {
    @GET("/")
    fun getRegistryTokenFromDockerIO(@Tag authSettings: DockerIoAuthSettings): Call<DockerIoRegistryToken>
}