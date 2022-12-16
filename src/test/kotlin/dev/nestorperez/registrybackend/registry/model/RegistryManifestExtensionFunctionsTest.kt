package dev.nestorperez.registrybackend.registry.model

import dev.nestorperez.registrybackend.schema.FsLayer
import dev.nestorperez.registrybackend.schema.History
import dev.nestorperez.registrybackend.schema.TagListData
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class RegistryManifestExtensionFunctionsTest {

    @Test
    fun toTagListData() {
        val original =
            RegistryManifest(
                schemaVersion = 1,
                name = "name",
                tag = "tagname",
                architecture = "arm64",
                fsLayers = listOf(RegistryFsLayer("blobSum")),
                history = listOf(RegistryHistory("v1 c")),
                signatures = listOf(
                    RegistryManifestSignature(
                        header = RegistrySignatureHeader(
                            jwk = RegistryHeaderJwk(
                                crv = "crv",
                                kid = "kid",
                                kty = "kty",
                                x = "x",
                                y = "y"
                            ), alg = "sha256"
                        ),
                        signature = "sign",
                        protected = "protected"
                    )
                )
            )
        val expected = TagListData(
            schemaVersion = 1,
            name = "name",
            architecture = "arm64",
            digest = "digest1",
            fsLayers = listOf(FsLayer("blobSum")),
            tag = "tagname",
            history = listOf(History("v1 c")),
            error = null
        )
        assertEquals(expected, original.toTagListData(digest = "digest1"))
    }
}