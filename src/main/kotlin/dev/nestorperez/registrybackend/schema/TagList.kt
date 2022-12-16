package dev.nestorperez.registrybackend.schema

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.extensions.getValuesFromDataLoader
import dev.nestorperez.registrybackend.dataloaders.TagListDataLoader
import dev.nestorperez.registrybackend.dataloaders.TagListDataLoaderInput
import dev.nestorperez.registrybackend.util.SkipCoverage
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletableFuture


//Skip coverage because for some reason constructor args are taken into consideration for coverage
@SkipCoverage
class TagList(
    @GraphQLDescription("Name of the repository")
    val name: String,
    @GraphQLIgnore
    val registryTagList: List<String>,
    @GraphQLIgnore
    val context: Context,
) {
    @GraphQLDescription("Tag list of the repository")
    @GraphQLName("tagList")
    fun tagList(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<List<TagListData>> {
        return dataFetchingEnvironment.getValuesFromDataLoader(
            TagListDataLoader.name,
            registryTagList.map { tag -> TagListDataLoaderInput(context, name, tag) }
        )
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + registryTagList.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TagList

        if (name != other.name) return false
        if (registryTagList != other.registryTagList) return false
        if (context != other.context) return false

        return true
    }
}

@SkipCoverage
data class TagListData(
    @GraphQLDescription("Schema version")
    val schemaVersion: Int?,
    @GraphQLDescription("Name of the repository")
    val name: String?,
    @GraphQLDescription("Name of the tag")
    val tag: String?,
    val architecture: String?,
    val fsLayers: List<FsLayer>?,
    @GraphQLDescription("Content digest")
    val digest: String?,
    @GraphQLDescription("History list")
    val history: List<History>?,
    @GraphQLDescription("If there's an error during data fetching this property will be the error")
    val error: String?
)

@SkipCoverage
data class FsLayer(val bobSum: String)

@SkipCoverage
data class History(
    @GraphQLDescription("JSON Encoded history")
    val v1Compatibility: String
)
