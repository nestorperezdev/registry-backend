package dev.nestorperez.registrybackend.query

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Query
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.RegistryCatalog
import dev.nestorperez.registrybackend.registry.model.toCatalog
import dev.nestorperez.registrybackend.schema.Catalog
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.util.handleIfSuccessOrError
import dev.nestorperez.registrybackend.util.valueOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import retrofit2.Response


@Controller
class AllRepositoriesQuery : Query {

    @Autowired
    lateinit var registryApi: RegistryApi

    suspend fun repositoriesQuery(
        context: Context,
        @GraphQLDescription("The number of repositories to return")
        first: OptionalInput<Int> = OptionalInput.Undefined,
        @GraphQLDescription("The number of repositories to skip")
        after: OptionalInput<Int> = OptionalInput.Undefined
    ): Catalog =
        registryApi
            .catalog(
                context = context,
                skip = after.valueOrNull(),
                take = first.valueOrNull()
            )
            .handleResponse(context)
}

private fun Response<RegistryCatalog>.handleResponse(context: Context): Catalog {
    return this.handleIfSuccessOrError(
        success = { it.toCatalog(context) },
        error = { Catalog(clientUrl = null, error = it, repositories = null, context = context) }
    )
}
