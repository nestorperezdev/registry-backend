package dev.nestorperez.registrybackend.registry

import dev.nestorperez.registrybackend.schema.Context
import okhttp3.Request

fun findContextOnRequest(request: Request) = request.tag(Context::class.java)
