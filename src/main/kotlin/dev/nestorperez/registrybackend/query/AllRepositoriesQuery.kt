package dev.nestorperez.registrybackend.query

import com.expediagroup.graphql.server.operations.Query
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.registry.model.RegistryCatalog
import dev.nestorperez.registrybackend.registry.RegistryApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller


@Controller
class AllRepositoriesQuery : Query {

    @Autowired
    lateinit var registryApi: RegistryApi

    suspend fun repositoriesQuery(context: Context): RegistryCatalog = registryApi.catalog(context)
}