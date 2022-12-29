package dev.nestorperez.registrybackend.registry.model

import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList

fun RegistryTagList.toTagListGraphql(
    context: Context,
    take: Int? = null,
    after: Int? = null
): TagList {
    return TagList(
        name = this.name,
        registryTagList = this.tags.paginateTags(after, take),
        context = context,
        error = null
    )
}

/**
 * Given a tag list, it returns a new tag list with the first and after parameters applied
 */
fun List<String>.paginateTags(after: Int?, take: Int?): List<String> {
    if (after == null && take == null) return this
    return this
        .drop(after ?: 0)
        .take(take ?: this.size)
}