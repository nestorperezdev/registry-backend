package dev.nestorperez.registrybackend.query

import com.expediagroup.graphql.server.operations.Query
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.RegistryCatalog
import dev.nestorperez.registrybackend.registry.model.toCatalog
import dev.nestorperez.registrybackend.schema.Catalog
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.util.handleIfSuccessOrError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import retrofit2.Response


@Controller
class AllRepositoriesQuery : Query {

    @Autowired
    lateinit var registryApi: RegistryApi

    suspend fun repositoriesQuery(context: Context): Catalog =
        registryApi
            .catalog(context)
            .handleResponse(context)
}

private fun Response<RegistryCatalog>.handleResponse(context: Context): Catalog {
    return this.handleIfSuccessOrError(
        success = { it.toCatalog(context) },
        error = { Catalog(clientUrl = null, error = it, repositories = null, context = context) }
    )
}
