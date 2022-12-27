package dev.nestorperez.registrybackend.registry.interceptors

import dev.nestorperez.registrybackend.registry.findDockerIoAuthSettingsOnRequest
import dev.nestorperez.registrybackend.schema.DockerIoAuthSettings
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class DockerIoTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val dockerIoTokenAuthSettings = findDockerIoAuthSettingsOnRequest(request)
        dockerIoTokenAuthSettings?.let {
            val builder = request.newBuilder()
            buildUrlForDockerIo(builder, dockerIoTokenAuthSettings)
            return chain.proceed(builder.build())
        }
        return chain.proceed(request)
    }

    /**
     * Given the DockerIOAuthSettings modify the request url to request the token
     */
    private fun buildUrlForDockerIo(builder: Request.Builder, dockerIoAuthSettings: DockerIoAuthSettings) {
        val url =
            "${dockerIoAuthSettings.tokenUrl}?service=${dockerIoAuthSettings.service}&scope=${dockerIoAuthSettings.scope}"
        builder.url(url)
    }
}