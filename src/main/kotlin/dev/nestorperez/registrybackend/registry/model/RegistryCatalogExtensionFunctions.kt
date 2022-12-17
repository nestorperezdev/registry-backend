package dev.nestorperez.registrybackend.registry.model

import dev.nestorperez.registrybackend.schema.Catalog
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.Repository

fun RegistryCatalog.toCatalog(context: Context): Catalog =
    Catalog(
        clientUrl = context.url,
        repositories = this.repositories.map { Repository(repositoryName = it, context = context) },
        context = context
    )