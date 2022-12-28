package dev.nestorperez.registrybackend.registry

import dev.nestorperez.registrybackend.schema.Context
import dev.nestorperez.registrybackend.schema.DockerIoAuthSettings
import okhttp3.Request

fun findContextOnRequest(request: Request) = request.tag(Context::class.java)

fun findDockerIoAuthSettingsOnRequest(request: Request) = request.tag(DockerIoAuthSettings::class.java)
