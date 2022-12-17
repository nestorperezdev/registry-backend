package dev.nestorperez.registrybackend.dataloaders

import com.ninjasquad.springmockk.MockkBean
import dev.nestorperez.registrybackend.query.TagListQuery
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList
import dev.nestorperez.registrybackend.util.equalsIgnoreOrder
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest

@WebFluxTest(TagListLoader::class)
class TagListLoaderTest {
    @MockkBean
    lateinit var tagListQuery: TagListQuery

    @Autowired
    lateinit var sut: TagListLoader

    @Test
    fun `batchLoader on empty list`() = runBlocking {
        assertEquals(sut.batchLoader(emptyList()), emptyList<TagList>())
    }


    @Test
    fun `should test completableFuture`() {
        val future = sut.completableFuture(emptyList())
        assertEquals(future.get(), emptyList<TagList>())
    }

    @Test
    fun `should fetch data from registryApi`() = runBlocking {
        val ctx: Context = mockk()
        val input = listOf(
            TagListLoaderInput(context = ctx, repositoryName = "repoName"),
            TagListLoaderInput(context = ctx, repositoryName = "repoName1"),
            TagListLoaderInput(context = ctx, repositoryName = "repoName2"),
        )
        coEvery { tagListQuery.tagsListQuery(ctx, any()) } returns
                TagList(name = "repoName", registryTagList = listOf("1.1", "1.2"), context = ctx, error = null) andThen
                TagList(name = "repoName1", registryTagList = null, context = ctx, error = "Error fetching data") andThen
                TagList(name = "repoName2", registryTagList = null, context = ctx, error = "Error fetching data")
        val result = sut.batchLoader(input)
        val expectedResult = listOf(
            TagList(name = "repoName", registryTagList = listOf("1.1", "1.2"), context = ctx, error = null),
            TagList(name = "repoName1", registryTagList = null, context = ctx, error = "Error fetching data"),
            TagList(name = "repoName2", registryTagList = null, context = ctx, error = "Error fetching data")
        )
        assertTrue(result equalsIgnoreOrder expectedResult)
        coVerify(exactly = 3) { tagListQuery.tagsListQuery(ctx, any()) }
    }
}