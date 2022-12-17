package dev.nestorperez.registrybackend.query

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
}