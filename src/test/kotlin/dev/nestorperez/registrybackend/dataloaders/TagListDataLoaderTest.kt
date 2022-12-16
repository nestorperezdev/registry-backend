package dev.nestorperez.registrybackend.dataloaders

import com.ninjasquad.springmockk.MockkBean
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.RegistryManifest
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagListData
import dev.nestorperez.registrybackend.util.buildApiResponse
import dev.nestorperez.registrybackend.util.equalsIgnoreOrder
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import retrofit2.Response

@WebFluxTest(TagListDataLoader::class)
class TagListDataLoaderTest {
    @MockkBean
    lateinit var registryApi: RegistryApi

    @Autowired
    lateinit var sut: TagListDataLoader

    @Test
    fun `batchLoader on empty list`() = runBlocking {
        assertEquals(sut.batchLoader(emptyList()), emptyList<TagListData>())
    }

    @Test
    fun `on data fetch error should present the error`() = runBlocking {
        val errorResponse: Response<RegistryManifest> = buildApiResponse(isSuccessful = false)
        val successBody = RegistryManifest(
            tag = "1.2",
            schemaVersion = 1,
            name = "reponame",
            architecture = "arm64",
            fsLayers = emptyList(),
            history = emptyList(),
            signatures = emptyList()
        )
        val successResponse = buildApiResponse(
            isSuccessful = true,
            body = successBody,
            extraHeaders = mapOf("docker-content-digest" to "docker-content-digest-value")
        )
        coEvery {
            registryApi.getManifest(
                context = any(),
                tag = any(),
                repositoryName = any()
            )
        } returns errorResponse andThen successResponse andThen buildApiResponse(isSuccessful = true, body = null)
        val mockedContext: Context = mockk()
        val inputList = listOf(
            TagListDataLoaderInput(context = mockedContext, tag = "1.1", repositoryName = "reponame"),
            TagListDataLoaderInput(context = mockedContext, tag = "1.2", repositoryName = "reponame"),
            TagListDataLoaderInput(context = mockedContext, tag = "1.3", repositoryName = "reponame"),
        )
        val expectedResponse = listOf(
            TagListData(
                tag = "1.1",
                history = null,
                fsLayers = null,
                digest = null,
                architecture = null,
                name = "reponame",
                schemaVersion = null,
                error = "Error fetching data"
            ),
            TagListData(
                tag = "1.2",
                history = emptyList(),
                fsLayers = emptyList(),
                digest = "docker-content-digest-value",
                architecture = "arm64",
                name = "reponame",
                schemaVersion = 1,
                error = null
            ),
            TagListData(
                tag = "1.3",
                history = null,
                fsLayers = null,
                digest = null,
                architecture = null,
                name = "reponame",
                schemaVersion = null,
                error = "Error fetching data"
            ),
        )
        assertTrue(sut.batchLoader(inputList) equalsIgnoreOrder expectedResponse)
        coVerifySequence {
            registryApi.getManifest(context = mockedContext, tag = "1.1", repositoryName = "reponame")
            registryApi.getManifest(context = mockedContext, tag = "1.2", repositoryName = "reponame")
            registryApi.getManifest(context = mockedContext, tag = "1.3", repositoryName = "reponame")
        }
    }


    @Test
    fun `should test completableFuture`() {
        val future = sut.completableFuture(emptyList())
        assertEquals(future.get(), emptyList<TagListData>())
    }
}
