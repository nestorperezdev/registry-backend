package dev.nestorperez.registrybackend.schema

import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import dev.nestorperez.registrybackend.dataloaders.TagListLoader
import dev.nestorperez.registrybackend.dataloaders.TagListLoaderInput
import graphql.schema.DataFetchingEnvironment
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture

class RepositoryTest {

    @Test
    fun tagList() = runBlocking {
        mockkStatic("com.expediagroup.graphql.server.extensions.DataFetchingEnvironmentExtensionsKt")
        val completableFuture: CompletableFuture<Any> = mockk()
        val ctx: Context = mockk()
        val dataFetchingEnvironment: DataFetchingEnvironment = mockk {
            every { getValueFromDataLoader<Any, Any>(any(), any()) }.returns(completableFuture)
        }
        val repository = Repository(repositoryName = "repoName", context = ctx)
        val result = repository.tagList(dataFetchingEnvironment)
        val expectedInputOfDataFetchingEnvironment = TagListLoaderInput(context = ctx, repositoryName = "repoName")
        assertEquals(completableFuture, result)
        verify(exactly = 1) {
            dataFetchingEnvironment
                .getValueFromDataLoader<Any, Any>(TagListLoader.name, expectedInputOfDataFetchingEnvironment)
        }
    }
}