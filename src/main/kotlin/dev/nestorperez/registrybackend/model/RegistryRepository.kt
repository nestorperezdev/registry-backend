package dev.nestorperez.registrybackend.model

import com.google.gson.annotations.SerializedName

data class RegistryRepository(
    @SerializedName("repositories")
    val repositories: List<String>
)
