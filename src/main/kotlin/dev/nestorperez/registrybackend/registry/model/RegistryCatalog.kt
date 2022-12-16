package dev.nestorperez.registrybackend.registry.model

import com.google.gson.annotations.SerializedName
import dev.nestorperez.registrybackend.util.SkipCoverage

@SkipCoverage
data class RegistryCatalog(
    @SerializedName("repositories")
    val repositories: List<String>
)
