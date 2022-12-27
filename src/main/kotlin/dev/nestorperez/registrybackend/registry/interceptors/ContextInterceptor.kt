package dev.nestorperez.registrybackend.registry.interceptors

import com.google.gson.Gson
import dev.nestorperez.registrybackend.authapi.AuthApi
import dev.nestorperez.registrybackend.registry.findContextOnRequest
import dev.nestorperez.registrybackend.schema.AuthSettings
import dev.nestorperez.registrybackend.service.JwtTokenStorageService
import dev.nestorperez.registrybackend.util.handleIfSuccessOrError
import dev.nestorperez.registrybackend.util.isJwtExpired
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.PrimitiveIterator


class ContextInterceptor(
    private val jwtTokenStorageService: JwtTokenStorageService,
    private val authApi: AuthApi,
    private val gson: Gson
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val context = findContextOnRequest(request)
        context?.let {
            val originalPath = request.url.pathSegments.joinToString("/")
            val builder = request.newBuilder().url("${context.url}/${originalPath}")
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
                "Authorization", Credentials.basic(authSettings.basicAuth.username, authSettings.basicAuth.password)
            )
        } else if (authSettings.jwtToken != null) {
            builder.addHeader("Authorization", "Bearer ${authSettings.jwtToken.token}")
        } else if (authSettings.dockerIoAuthSettings != null) {
            val token = jwtTokenStorageService.getToken(authSettings.dockerIoAuthSettings)
            if (token != null && !token.isJwtExpired(gson)) {
                // if token exists on JwtTokenStorageService retrieve it and add it to the request
                builder.addHeader("Authorization", "Bearer $token")
            } else {
                // if token does not exist, request a new token over RegistryApi and add it to the request after save it.
                authApi.getRegistryTokenFromDockerIO(authSettings.dockerIoAuthSettings).execute()
                    .handleIfSuccessOrError(
                        success = { tokenResponse ->
                            jwtTokenStorageService.saveToken(authSettings.dockerIoAuthSettings, tokenResponse.token)
                            builder.addHeader("Authorization", "Bearer ${tokenResponse.token}")
                        }, error = { error ->
                            println(error)
                            //  todo: handle error from auth service
                        }
                    )
            }
        }
    }
}