package dev.nestorperez.registrybackend.query

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.ninjasquad.springmockk.MockkBean
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.RegistryTagList
import dev.nestorperez.registrybackend.registry.model.toTagListGraphql
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList
import dev.nestorperez.registrybackend.util.buildApiResponse
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest

@WebFluxTest(TagListQuery::class)
class TagListQueryTest {
    @MockkBean
    lateinit var registryApi: RegistryApi

    @Autowired
    lateinit var sut: TagListQuery

    @Test
    fun tagsListQuery() = runBlocking {
        mockkStatic("dev.nestorperez.registrybackend.registry.model.RegistryTagListExtensionFunctionsKt")
        val ctx: Context = mockk()
        val repositoryName = "repoName"
        val mockedTransform: TagList = mockk()
        val mockedResponse: RegistryTagList = mockk {
            every { toTagListGraphql(any()) }.returns(mockedTransform)
        }
        coEvery { registryApi.tagsList(any(), any()) }.returns(
            buildApiResponse(
                isSuccessful = true,
                body = mockedResponse
            )
        )
        val result = sut.tagsListQuery(ctx, repositoryName)
        assertEquals(mockedTransform, result)
        coVerify(exactly = 1) { registryApi.tagsList(ctx, "repoName") }
        verify(exactly = 1) { mockedResponse.toTagListGraphql(ctx) }
    }

    @Test
    fun `on backend error`() = runBlocking {
        mockkStatic("dev.nestorperez.registrybackend.registry.model.RegistryTagListExtensionFunctionsKt")
        val ctx: Context = mockk()
        val repositoryName = "repoName"
        coEvery { registryApi.tagsList(any(), any()) }.returns(buildApiResponse(isSuccessful = false))
        val result = sut.tagsListQuery(ctx, repositoryName)
        assertEquals(
            TagList(
                name = repositoryName,
                registryTagList = null,
                context = ctx,
                error = "Error fetching data"
            ),
            result
        )
        coVerify(exactly = 1) { registryApi.tagsList(ctx, "repoName") }
    }

    @Test
    fun `should pass after and take to consumeAndConvertToTagList ext function`() = runBlocking {
        val ctx = Context(url = "http://localhost:5000", port = null, authSettings = null)
        val apiResponse = buildApiResponse(
            isSuccessful = true,
            body = RegistryTagList(
                name = "repoName",
                tags = listOf("tag1", "tag2", "tag3", "tag4", "tag5")
            )
        )
        coEvery { registryApi.tagsList(any(), any()) }.returns(apiResponse)
        val response = sut.tagsListQuery(ctx, "repoName", OptionalInput.Defined(2), OptionalInput.Defined(2))
        assertEquals(
            TagList(
                name = "repoName",
                registryTagList = listOf("tag3", "tag4"),
                context = ctx,
                error = null
            ),
            response
        )
    }

    @Test
    fun `should handle success with take and after parameters`() {
        val context = mockk<Context>()
        val successResponse = buildApiResponse(
            isSuccessful = true,
            body = RegistryTagList(
                name = "repoName",
                tags = listOf("tag1", "tag2", "tag3", "tag4", "tag5")
            )
        )
        val response = successResponse.consumeAndConvertToTagList(context, "repoName", 2, 2)
        assertEquals(2, response.registryTagList!!.size)
        assertEquals("tag3", response.registryTagList!![0])
        assertEquals("tag4", response.registryTagList!![1])
    }

    @Test
    fun `should handle success when take and after params are null`() {
        val context = mockk<Context>()
        val successResponse = buildApiResponse(
            isSuccessful = true,
            body = RegistryTagList(
                name = "repoName",
                tags = listOf("tag1", "tag2", "tag3", "tag4", "tag5")
            )
        )
        val response = successResponse.consumeAndConvertToTagList(context, "repoName", null, null)
        assertEquals(5, response.registryTagList!!.size)
        assertEquals("tag1", response.registryTagList!![0])
        assertEquals("tag5", response.registryTagList!![4])
    }
}