package dev.nestorperez.registrybackend.registry.model

import dev.nestorperez.registrybackend.schema.Catalog
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.Repository

fun RegistryCatalog.toCatalog(context: Context, filterByRepository: String?): Catalog {
    var repositories = this.repositories.map { Repository(repositoryName = it, context = context) }
    filterByRepository?.let { filter ->
        repositories = repositories.filter { it.repositoryName.lowercase().contains(filter.lowercase()) }
    }
    return Catalog(
        clientUrl = context.url,
        repositories = repositories,
        context = context,
        error = null
    )
}