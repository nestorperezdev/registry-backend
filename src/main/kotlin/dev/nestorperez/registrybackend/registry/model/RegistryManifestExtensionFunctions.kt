package dev.nestorperez.registrybackend.registry.model

import dev.nestorperez.registrybackend.schema.FsLayer
import dev.nestorperez.registrybackend.schema.History
import dev.nestorperez.registrybackend.schema.TagListData

fun RegistryManifest.toTagListData(digest: String?): TagListData {
    return TagListData(
        schemaVersion = this.schemaVersion,
        name = this.name,
        tag = this.tag,
        architecture = this.architecture,
        fsLayers = this.fsLayers.map { FsLayer(it.blobSum) },
        digest = digest,
        history = this.history.map { History(it.v1Compatibility) },
        error = null
    )
}