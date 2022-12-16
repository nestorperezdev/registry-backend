package dev.nestorperez.registrybackend.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.toTagListData
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagListData
import dev.nestorperez.registrybackend.util.SkipCoverage
import graphql.VisibleForTesting
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class TagListDataLoader(private val registryApi: RegistryApi) :
    KotlinDataLoader<TagListDataLoaderInput, TagListData> {

    companion object {
        const val name = "TagListDataLoader"
    }

    override val dataLoaderName = name

    //  not sure how to test this
    @SkipCoverage
    override fun getDataLoader(): DataLoader<TagListDataLoaderInput, TagListData> =
        DataLoaderFactory.newDataLoader(
            ::completableFuture,
            DataLoaderOptions.newOptions().setCachingEnabled(true)
        )

    @VisibleForTesting
    fun completableFuture(requestedTags: List<TagListDataLoaderInput>): CompletableFuture<List<TagListData>> {
        return GlobalScope.future { batchLoader(requestedTags) }
    }

    @VisibleForTesting
    suspend fun batchLoader(requestedTags: List<TagListDataLoaderInput>): List<TagListData> {
        return requestedTags.map { input ->
            val response = registryApi.getManifest(
                context = input.context,
                tag = input.tag,
                repositoryName = input.repositoryName
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                body.toTagListData(response.headers().get("docker-content-digest"))
            } else {
                TagListData(
                    schemaVersion = null,
                    name = input.repositoryName,
                    tag = input.tag,
                    architecture = null,
                    fsLayers = null,
                    digest = null,
                    history = null,
                    error = "Error fetching data"
                )
            }
        }
    }

}

@SkipCoverage
data class TagListDataLoaderInput(val context: Context, val repositoryName: String, val tag: String)