package dev.nestorperez.registrybackend.util

import com.expediagroup.graphql.generator.execution.OptionalInput

fun <T> OptionalInput<T>.valueOrNull(): T? = when (this) {
    is OptionalInput.Undefined -> null
    is OptionalInput.Defined -> this.value
}
