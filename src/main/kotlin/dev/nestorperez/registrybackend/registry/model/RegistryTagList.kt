package dev.nestorperez.registrybackend.registry.model

import com.google.gson.annotations.SerializedName
import dev.nestorperez.registrybackend.util.SkipCoverage

@SkipCoverage
data class RegistryTagList(@SerializedName("name") val name: String, @SerializedName("tags") val tags: List<String>) {
}
