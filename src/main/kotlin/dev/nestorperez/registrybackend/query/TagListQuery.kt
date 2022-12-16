package dev.nestorperez.registrybackend.query

import com.expediagroup.graphql.server.operations.Query
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.toTagListGraphql
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller


@Controller
class TagListQuery : Query {

    @Autowired
    lateinit var registryApi: RegistryApi

    suspend fun tagsListQuery(context: Context, repositoryName: String): TagList =
        registryApi
            .tagsList(context, repositoryName)
            .toTagListGraphql(context)
}
