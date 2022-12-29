package dev.nestorperez.registrybackend.registry.interceptors

import dev.nestorperez.registrybackend.registry.findContextOnRequest
import dev.nestorperez.registrybackend.schema.AuthSettings
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl


class ContextInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val context = findContextOnRequest(request)
        context?.let {
            val originalBuilder = request.url.newBuilder()
            val contextUrl = context.url.toHttpUrl()
            originalBuilder
                .host(contextUrl.host)
                .scheme(contextUrl.scheme)
                .port(contextUrl.port)
            val builder = request
                .newBuilder()
                .url(originalBuilder.build())
            it.authSettings?.let { auth ->
                includeAuthSettingsInResponse(builder, auth)
            }
            request = builder.build()
        }
        return chain.proceed(request)
    }

    /**
     * Determines if any authentication method is different from null in authSettings
     * if so, it adds the corresponding header to the request.
     */
    private fun includeAuthSettingsInResponse(builder: Request.Builder, authSettings: AuthSettings) {
        if (authSettings.basicAuth != null) {
            builder.addHeader(
                "Authorization",
                Credentials.basic(authSettings.basicAuth.username, authSettings.basicAuth.password)
            )
        } else if (authSettings.jwtToken != null) {
            builder.addHeader("Authorization", "Bearer ${authSettings.jwtToken.token}")
        }
    }
}