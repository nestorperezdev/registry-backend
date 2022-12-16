package dev.nestorperez.registrybackend.registry.model

import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList

fun RegistryTagList.toTagListGraphql(context: Context): TagList {
    return TagList(name = this.name, registryTagList = this.tags, context = context)
}