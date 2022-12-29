package dev.nestorperez.registrybackend.registry.model

import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RegistryTagListExtensionFunctionsTest {

    @Test
    fun toTagListGraphql() {
        val ctx: Context = mockk()
        assertEquals(
            RegistryTagList(name = "name", tags = listOf("1", "2")).toTagListGraphql(ctx),
            TagList(name = "name", registryTagList = listOf("1", "2"), context = ctx, error = null)
        )
    }

    @Test
    fun `should return a sublist of paginated items`() {
        val after = 10
        val take = 5
        val list = (0..20).map { it.toString() }
        list.paginateTags(after, take).let {
            assertEquals(it.size, take)
            assertEquals(it.first(), list[after])
            assertEquals(it.last(), list[after + take - 1])
        }
    }

    @Test
    fun `should return the full list when after and take are null`() {
        val list = (0..20).map { it.toString() }
        list.paginateTags(null, null).let {
            assertEquals(it.size, list.size)
            assertEquals(it.first(), list.first())
            assertEquals(it.last(), list.last())
        }
    }

    @Test
    fun `when after is null should return the first 5 elements`() {
        val after = null
        val take = 5
        val list = (0..20).map { it.toString() }
        list.paginateTags(after, take).let {
            assertEquals(it.size, take)
            assertEquals(it.first(), list.first())
            assertEquals(it.last(), list[take - 1])
        }
    }

    @Test
    fun `when take is null should return the last 5 elements`() {
        val after = 15
        val take = null
        val list = (0..20).map { it.toString() }
        list.paginateTags(after, take).let {
            assertEquals(it.size, list.size - after)
            assertEquals(it.first(), list[after])
            assertEquals(it.last(), list.last())
        }
    }
}