package dev.nestorperez.registrybackend.registry.model

import com.google.gson.annotations.SerializedName
import dev.nestorperez.registrybackend.util.SkipCoverage

@SkipCoverage
data class RegistryManifest(
    @SerializedName("schemaVersion")
    val schemaVersion: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("tag")
    val tag: String,
    @SerializedName("architecture")
    val architecture: String,
    @SerializedName("fsLayers")
    val fsLayers: List<RegistryFsLayer>,
    @SerializedName("history")
    val history: List<RegistryHistory>,
    @SerializedName("signatures")
    val signatures: List<RegistryManifestSignature>,
)

@SkipCoverage
data class RegistryFsLayer(
    @SerializedName("blobSum")
    val blobSum: String
)

@SkipCoverage
data class RegistryHistory(
    @SerializedName("v1Compatibility")
    val v1Compatibility: String
)

@SkipCoverage
data class RegistryManifestSignature(
    val header: RegistrySignatureHeader, val signature: String, val protected: String
)

@SkipCoverage
class RegistrySignatureHeader(val jwk: RegistryHeaderJwk, alg: String)

@SkipCoverage
class RegistryHeaderJwk(val crv: String, val kid: String, val kty: String, val x: String, val y: String)
