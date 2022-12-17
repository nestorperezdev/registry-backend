package dev.nestorperez.registrybackend.query

import com.ninjasquad.springmockk.MockkBean
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.model.RegistryCatalog
import dev.nestorperez.registrybackend.schema.Context
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import retrofit2.Response

@WebFluxTest(AllRepositoriesQuery::class)
class AllRepositoriesQueryTest {
    @MockkBean
    lateinit var registryApi: RegistryApi

    @Autowired
    lateinit var sut: AllRepositoriesQuery

    @Test
    fun `should call registry api catalog`() = runBlocking {
        val context = Context("http://localhost:5000", authSettings = null, port = null)
        val registryCatalog = RegistryCatalog(listOf("repo1", "repo2"))
        val response: Response<RegistryCatalog> = Response.success(registryCatalog)
        coEvery { registryApi.catalog(context) } returns response

        sut.repositoriesQuery(context)

        coVerify { registryApi.catalog(context) }
    }

    @Test
    fun `when response is unsuccessful should return error`() = runBlocking {
        val context = Context("http://localhost:5000", authSettings = null, port = null)
        val okHttpResponse = okhttp3.Response.Builder()
            .code(500)
            .message("Internal Server Error")
            .body(mockk())
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .request(okhttp3.Request.Builder().url("http://localhost:5000").build())
            .build()
        val response: Response<RegistryCatalog> = Response.error(okHttpResponse.body!!, okHttpResponse)
        coEvery { registryApi.catalog(context) } returns response

        val result = sut.repositoriesQuery(context)

        coVerify { registryApi.catalog(context) }
        assertEquals("Internal Server Error", result.error)
    }

}