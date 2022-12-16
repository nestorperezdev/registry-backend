package dev.nestorperez.registrybackend.schema

import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.expediagroup.graphql.server.extensions.getValuesFromDataLoader
import dev.nestorperez.registrybackend.dataloaders.TagListDataLoader
import dev.nestorperez.registrybackend.dataloaders.TagListDataLoaderInput
import graphql.schema.DataFetchingEnvironment
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture

class TagListTest {
    @Test
    fun `should fetch a list of tagList`() {
        mockkStatic("com.expediagroup.graphql.server.extensions.DataFetchingEnvironmentExtensionsKt")
        val registryTagList: List<String> = listOf("tag1", "tag2")
        val context: Context = mockk()
        val completableFuture: CompletableFuture<List<Any>> = mockk()
        val dataFetchingEnvironment: DataFetchingEnvironment = mockk {
            every { getValuesFromDataLoader<Any, Any>(any(), any()) }.returns(completableFuture)
        }
        val tagList = TagList(name = "name", registryTagList = registryTagList, context = context)
        val result = tagList.tagList(dataFetchingEnvironment)
        assertEquals(completableFuture, result)
        val expectedInputOfDataFetchingEnvironment = registryTagList.map { TagListDataLoaderInput(context, "name", it) }
        verify(exactly = 1) {
            dataFetchingEnvironment
                .getValuesFromDataLoader<Any, Any>(TagListDataLoader.name, expectedInputOfDataFetchingEnvironment)
        }
    }

    @Test
    fun `test should be same class`() {
        val ctx: Context = mockk()
        val tagListStr: List<String> = emptyList()
        val tagList = TagList(name = "name", registryTagList = tagListStr, context = ctx)
        assertEquals(tagList, TagList(name = "name", registryTagList = tagListStr, context = ctx))
    }

    @Test
    fun `should be different data`() {
        val ctx: Context = mockk()
        val tagListStr: List<String> = emptyList()
        val tagList = TagList(name = "name1", registryTagList = tagListStr, context = ctx)
        assertNotEquals(tagList, TagList(name = "name", registryTagList = tagListStr, context = ctx))
    }

    @Test
    fun `should be different data taglist`() {
        val ctx: Context = mockk()
        val tagListStr: List<String> = emptyList()
        val tagListStr1: List<String> = listOf("1")
        val tagList = TagList(name = "name", registryTagList = tagListStr, context = ctx)
        assertNotEquals(tagList, TagList(name = "name", registryTagList = tagListStr1, context = ctx))
    }

    @Test
    fun `should be different data ctx`() {
        val ctx: Context = mockk()
        val ctx1: Context = mockk()
        val tagListStr: List<String> = emptyList()
        val tagList = TagList(name = "name", registryTagList = tagListStr, context = ctx)
        assertNotEquals(tagList, TagList(name = "name", registryTagList = tagListStr, context = ctx1))
    }

    @Test
    fun `should be same hashCode`() {
        val ctx: Context = mockk()
        val tagListStr: List<String> = emptyList()
        val tagList = TagList(name = "name", registryTagList = tagListStr, context = ctx)
        assertEquals(tagList.hashCode(), TagList(name = "name", registryTagList = tagListStr, context = ctx).hashCode())
    }

    @Test
    fun `class should be equals to same class`() {
        val tagList = TagList(name = "name", registryTagList = emptyList(), context = mockk())
        assertEquals(tagList, tagList)
    }

    @Test
    fun `not the same instance`() {
        val tagList = TagList(name = "name", registryTagList = emptyList(), context = mockk())
        val ctx: Context = mockk()
        assertNotEquals(tagList, ctx)
    }

    @Test
    fun `null is not the same`() {
        val tagList = TagList(name = "name", registryTagList = emptyList(), context = mockk())
        assertFalse(tagList.equals(null))
    }
}