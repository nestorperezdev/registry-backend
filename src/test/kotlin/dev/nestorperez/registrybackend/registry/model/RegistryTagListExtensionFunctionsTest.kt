package dev.nestorperez.registrybackend.registry.model

import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.TagList
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.coroutines.coroutineContext

class RegistryTagListExtensionFunctionsTest {

    @Test
    fun toTagListGraphql() {
        val ctx: Context = mockk()
        assertEquals(
            RegistryTagList(name = "name", tags = listOf("1", "2")).toTagListGraphql(ctx),
            TagList(name = "name", registryTagList = listOf("1", "2"), context = ctx)
        )
    }
}