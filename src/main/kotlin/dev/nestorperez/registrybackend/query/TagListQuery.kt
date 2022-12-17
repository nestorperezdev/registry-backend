package dev.nestorperez.registrybackend.query

import com.expediagroup.graphql.server.operations.Query
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.RegistryTagList
import dev.nestorperez.registrybackend.registry.model.toTagListGraphql
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList
import dev.nestorperez.registrybackend.util.consumeResponseIfValid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import retrofit2.Response


@Controller
class TagListQuery : Query {

    @Autowired
    lateinit var registryApi: RegistryApi

    suspend fun tagsListQuery(context: Context, repositoryName: String): TagList =
        registryApi
            .tagsList(context, repositoryName)
            .consumeAndConvertToTagList(context, repositoryName)
}

fun Response<RegistryTagList>.consumeAndConvertToTagList(context: Context, repositoryName: String) =
    this.consumeResponseIfValid {
        it.toTagListGraphql(context)
    } ?: kotlin.run {
        TagList(
            name = repositoryName,
            registryTagList = null,
            context = context,
            error = "Error fetching data"
        )
    }
