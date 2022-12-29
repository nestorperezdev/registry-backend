package dev.nestorperez.registrybackend

import dev.nestorperez.registrybackend.registry.interceptors.ContextInterceptor
import dev.nestorperez.registrybackend.registry.RegistryApi
import dev.nestorperez.registrybackend.registry.interceptors.ErrorHandlingInterceptor
import dev.nestorperez.registrybackend.util.SkipCoverage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SpringBootApplication
class RegistryBackendApplication {

    /**
     * If current active profile is not production, then set the log level to body otherwise set it to basic
     */
    fun loggingInterceptor(environment: Environment) = HttpLoggingInterceptor().apply {
        level = if (environment.activeProfiles.contains("prod")) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.BODY
        }
    }

    @Bean
    fun okHttpClient(environment: Environment) = OkHttpClient.Builder()
        .addInterceptor(ContextInterceptor())
        .addInterceptor(ErrorHandlingInterceptor())
        .addInterceptor(loggingInterceptor(environment))
        .build()

    @Bean
    fun createRegistryClient(@Autowired client: OkHttpClient) =
        Retrofit.Builder()
            .client(client)
            .baseUrl("http://localhost/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RegistryApi::class.java)
}

@SkipCoverage
fun main(args: Array<String>) {
    runApplication<RegistryBackendApplication>(*args)
}
