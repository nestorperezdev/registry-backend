package dev.nestorperez.registrybackend.registry

import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.registry.model.RegistryCatalog
import dev.nestorperez.registrybackend.registry.model.RegistryManifest
import dev.nestorperez.registrybackend.registry.model.RegistryTagList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Tag

interface RegistryApi {
    @GET("/v2/_catalog")
    suspend fun catalog(
        @Tag context: Context,
        @Query("last") skip: Int? = null,
        @Query("n") take: Int? = null
    ): Response<RegistryCatalog>

    @GET("/v2/{repositoryName}/tags/list")
    suspend fun tagsList(
        @Tag context: Context,
        @Path(value = "repositoryName", encoded = true) repositoryName: String
    ): Response<RegistryTagList>

    @GET("/v2/{repositoryName}/manifests/{tag}")
    suspend fun getManifest(
        @Tag context: Context,
        @Path(value = "repositoryName", encoded = true) repositoryName: String,
        @Path(value = "tag", encoded = true) tag: String
    ): Response<RegistryManifest>
}