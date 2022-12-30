package dev.nestorperez.registrybackend.schema

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import dev.nestorperez.registrybackend.dataloaders.TagListLoader
import dev.nestorperez.registrybackend.dataloaders.TagListLoaderInput
import dev.nestorperez.registrybackend.util.SkipCoverage
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletableFuture

@SkipCoverage
class Catalog(
    @GraphQLDescription("Client URL")
    val clientUrl: String?,
    @GraphQLDescription("In case of error with the request")
    val error: String?,
    @GraphQLDescription("Repositories list")
    val repositories: List<Repository>?,
    @GraphQLIgnore
    val context: Context
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Catalog

        if (clientUrl != other.clientUrl) return false
        if (error != other.error) return false
        if (repositories != other.repositories) return false
        if (context != other.context) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clientUrl?.hashCode() ?: 0
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + (repositories?.hashCode() ?: 0)
        result = 31 * result + context.hashCode()
        return result
    }
}

@SkipCoverage
class Repository(
    @GraphQLDescription("Name of the repository")
    val repositoryName: String,
    @GraphQLIgnore
    val context: Context
) {
    @GraphQLDescription("List of tags on this repository")
    @GraphQLName("tagList")
    fun tagList(
        dataFetchingEnvironment: DataFetchingEnvironment,
        take: OptionalInput<Int> = OptionalInput.Undefined,
        after: OptionalInput<Int> = OptionalInput.Undefined
    ): CompletableFuture<TagList> {
        return dataFetchingEnvironment.getValueFromDataLoader(
            TagListLoader.name,
            TagListLoaderInput(context = context, repositoryName = repositoryName, take = take, after = after)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Repository

        if (repositoryName != other.repositoryName) return false
        if (context != other.context) return false

        return true
    }

    override fun hashCode(): Int {
        var result = repositoryName.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }


}
