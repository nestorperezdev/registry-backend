package dev.nestorperez.registrybackend.util

import com.expediagroup.graphql.generator.execution.OptionalInput
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class OptionalInputValueOrNull {
    @Test
    fun `when optional is undefined value should be null`() {
        val optional: OptionalInput<Int> = OptionalInput.Undefined
        assertNull(optional.valueOrNull())
    }

    @Test
    fun `when optional is defined value should be null`() {
        val optional: OptionalInput<Int> = OptionalInput.Defined(1)
        assertEquals(optional.valueOrNull(), 1)
    }
}