package dev.nestorperez.registrybackend

import com.google.gson.Gson
import dev.nestorperez.registrybackend.authapi.AuthApi
import dev.nestorperez.registrybackend.registry.interceptors.ContextInterceptor
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.interceptors.DockerIoTokenInterceptor
import dev.nestorperez.registrybackend.registry.interceptors.ErrorHandlingInterceptor
import dev.nestorperez.registrybackend.service.JwtTokenStorageService
import dev.nestorperez.registrybackend.util.SkipCoverage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.GsonFactoryBean
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SpringBootApplication
class RegistryBackendApplication {

    @Bean
    fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Bean("clientForAuth")
    fun okHttpClientAuth(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient
        .Builder()
        .addInterceptor(DockerIoTokenInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    @Bean
    fun createAuthClient(@Qualifier("clientForAuth") @Autowired client: OkHttpClient) =
        Retrofit.Builder()
            .client(client)
            .baseUrl("http://localhost/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)

    @Bean
    fun gson() = Gson()

    @Bean("clientForRegistry")
    fun okHttpClientRegistry(
        @Autowired authApi: AuthApi,
        @Autowired jwtTokenStorageService: JwtTokenStorageService,
        @Autowired gson: Gson,
        loggingInterceptor: HttpLoggingInterceptor
    ) =
        OkHttpClient
            .Builder()
            .addInterceptor(
                ContextInterceptor(
                    authApi = authApi,
                    jwtTokenStorageService = jwtTokenStorageService,
                    gson = gson
                )
            )
            .addInterceptor(ErrorHandlingInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()

    @Bean
    fun createRegistryClient(@Qualifier("clientForRegistry") @Autowired client: OkHttpClient, @Autowired gson: Gson) =
        Retrofit.Builder()
            .client(client)
            .baseUrl("http://localhost/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RegistryApi::class.java)
}

@SkipCoverage
fun main(args: Array<String>) {
    runApplication<RegistryBackendApplication>(*args)
}
