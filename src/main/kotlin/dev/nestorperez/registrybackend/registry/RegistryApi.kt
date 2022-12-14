package dev.nestorperez.registrybackend.registry

import dev.nestorperez.registrybackend.model.Context
import dev.nestorperez.registrybackend.model.RegistryRepository
import retrofit2.http.GET
import retrofit2.http.Tag

interface RegistryApi {
    @GET("/v2/_catalog")
    suspend fun catalog(@Tag context: Context): RegistryRepository
}