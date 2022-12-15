package dev.nestorperez.registrybackend.model

import com.google.gson.annotations.SerializedName
import dev.nestorperez.registrybackend.util.SkipCoverage

@SkipCoverage
data class RegistryRepository(
    @SerializedName("repositories")
    val repositories: List<String>
)
