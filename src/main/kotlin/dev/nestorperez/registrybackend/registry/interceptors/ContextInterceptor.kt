package dev.nestorperez.registrybackend.registry.interceptors

import dev.nestorperez.registrybackend.registry.findContextOnRequest
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response


class ContextInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val context = findContextOnRequest(request)
        context?.let {
            val originalPath = request.url.pathSegments.joinToString("/")
            val builder = request
                .newBuilder()
                .url("${context.url}/${originalPath}")
            it.authSettings?.let { auth ->
                builder.addHeader("Authorization", Credentials.basic(auth.username, auth.password))
            }
            request = builder.build()
        }
        return chain.proceed(request)
    }
}