package dev.nestorperez.registrybackend.query

import com.expediagroup.graphql.server.operations.Query
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.toCatalog
import dev.nestorperez.registrybackend.schema.Catalog
import dev.nestorperez.registrybackend.schema.Context
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller


@Controller
class AllRepositoriesQuery : Query {

    @Autowired
    lateinit var registryApi: RegistryApi

    suspend fun repositoriesQuery(context: Context): Catalog = registryApi.catalog(context).toCatalog(context)
}