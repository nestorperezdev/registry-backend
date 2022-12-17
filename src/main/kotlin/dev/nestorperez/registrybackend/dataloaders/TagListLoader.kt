package dev.nestorperez.registrybackend.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import dev.nestorperez.registrybackend.query.TagListQuery
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList
import dev.nestorperez.registrybackend.util.SkipCoverage
import graphql.VisibleForTesting
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class TagListLoader(private val tagListQuery: TagListQuery) : KotlinDataLoader<TagListLoaderInput, TagList> {

    companion object {
        const val name = "TagListLoader"
    }

    override val dataLoaderName = name

    //  not sure how to test this
    @SkipCoverage
    override fun getDataLoader(): DataLoader<TagListLoaderInput, TagList> = DataLoaderFactory.newDataLoader(
        ::completableFuture, DataLoaderOptions.newOptions().setCachingEnabled(true)
    )

    @OptIn(DelicateCoroutinesApi::class)
    @VisibleForTesting
    fun completableFuture(tags: List<TagListLoaderInput>): CompletableFuture<List<TagList>> {
        return GlobalScope.future { batchLoader(tags) }
    }

    @VisibleForTesting
    suspend fun batchLoader(tags: List<TagListLoaderInput>): List<TagList> {
        return tags.map { input ->
            tagListQuery.tagsListQuery(input.context, input.repositoryName)
        }
    }


}

@SkipCoverage
data class TagListLoaderInput(val context: Context, val repositoryName: String)