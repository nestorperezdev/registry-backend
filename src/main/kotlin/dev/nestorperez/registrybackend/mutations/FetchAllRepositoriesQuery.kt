package dev.nestorperez.registrybackend.mutations

import com.expediagroup.graphql.server.operations.Query
import dev.nestorperez.registrybackend.model.Context
import dev.nestorperez.registrybackend.model.RegistryRepository
import dev.nestorperez.registrybackend.registry.RegistryApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller


@Controller
class FetchAllRepositoriesQuery : Query {

    @Autowired
    lateinit var registryApi: RegistryApi

    suspend fun repositories(context: Context): RegistryRepository = registryApi.catalog(context)
}