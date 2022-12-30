package dev.nestorperez.registrybackend.query

import com.expediagroup.graphql.generator.annotations.GraphQLDeprecated
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Query
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.RegistryTagList
import dev.nestorperez.registrybackend.registry.model.toTagListGraphql
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList
import dev.nestorperez.registrybackend.util.SkipCoverage
import dev.nestorperez.registrybackend.util.handleIfSuccessOrError
import dev.nestorperez.registrybackend.util.valueOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import retrofit2.Response


@Controller
class TagListQuery : Query {

    @Autowired
    lateinit var registryApi: RegistryApi
    @GraphQLDeprecated("Use repositoriesQuery instead")
    suspend fun tagsListQuery(
        context: Context, repositoryName: String,
        @GraphQLDescription("The number of tags to return")
        take: OptionalInput<Int> = OptionalInput.Undefined,
        @GraphQLDescription("The tag name to skip")
        after: OptionalInput<Int> = OptionalInput.Undefined
    ): TagList =
        registryApi
            .tagsList(context, repositoryName)
            .consumeAndConvertToTagList(
                context = context,
                repositoryName = repositoryName,
                take = take.valueOrNull(),
                after = after.valueOrNull()
            )
}

fun Response<RegistryTagList>.consumeAndConvertToTagList(
    context: Context,
    repositoryName: String,
    take: Int?,
    after: Int?
) =
    this.handleIfSuccessOrError(
        success = { it.toTagListGraphql(context, take, after) },
        error = {
            TagList(
                name = repositoryName,
                registryTagList = null,
                context = context,
                error = it
            )
        }
    )
