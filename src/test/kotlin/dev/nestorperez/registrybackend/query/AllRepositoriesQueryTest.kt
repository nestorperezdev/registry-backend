package dev.nestorperez.registrybackend.query

import com.ninjasquad.springmockk.MockkBean
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.registry.RegistryApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest

@WebFluxTest(AllRepositoriesQuery::class)
class AllRepositoriesQueryTest {
    @MockkBean
    lateinit var registryApi: RegistryApi

    @Autowired
    lateinit var sut: AllRepositoriesQuery

    @Test
    fun `should call catalog on repository with the correct context`(): Unit = runBlocking {
        coEvery { registryApi.catalog(any()) }.returns(mockk())
        val context: Context = mockk()
        sut.repositoriesQuery(context)
        coVerify(exactly = 1) { registryApi.catalog(context) }
    }
}