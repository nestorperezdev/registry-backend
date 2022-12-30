package dev.nestorperez.registrybackend.registry.model

import dev.nestorperez.registrybackend.schema.Catalog
import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.Repository
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RegistryCatalogExtensionFunctionsTest {
    @Test
    fun `should convert registry catalog to catalog`() {
        val registryCatalog = RegistryCatalog(listOf("repo1", "repo2"))
        val ctx = Context("http://localhost", port = null, authSettings = null)
        val catalog = registryCatalog.toCatalog(ctx, null)
        val expectedResult = Catalog(
            clientUrl = "http://localhost",
            repositories = listOf(
                Repository(repositoryName = "repo1", context = ctx),
                Repository(repositoryName = "repo2", context = ctx)
            ),
            context = ctx,
            error = null
        )
        assertEquals(expectedResult, catalog)
    }

    @Test
    fun `should convert registry catalog to with filtered by repo name`() {
        val registryCatalog = RegistryCatalog(listOf("repo1", "repo2"))
        val ctx = Context("http://localhost", port = null, authSettings = null)
        val catalog = registryCatalog.toCatalog(ctx, "1")
        val expectedResult = Catalog(
            clientUrl = "http://localhost",
            repositories = listOf(
                Repository(repositoryName = "repo1", context = ctx)
            ),
            context = ctx,
            error = null
        )
        assertEquals(expectedResult, catalog)
    }

    @Test
    fun `should convert registry catalog to with filtered by repo even if uppercase`() {
        val registryCatalog = RegistryCatalog(listOf("REPO SURNAME", "REPO NAME"))
        val ctx = Context("http://localhost", port = null, authSettings = null)
        val catalog = registryCatalog.toCatalog(ctx, "name")
        val expectedResult = Catalog(
            clientUrl = "http://localhost",
            repositories = listOf(
                Repository(repositoryName = "REPO SURNAME", context = ctx),
                Repository(repositoryName = "REPO NAME", context = ctx),
            ),
            context = ctx,
            error = null
        )
        assertEquals(expectedResult, catalog)
    }
}