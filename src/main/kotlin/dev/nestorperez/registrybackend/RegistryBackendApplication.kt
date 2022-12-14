package dev.nestorperez.registrybackend

import dev.nestorperez.registrybackend.registry.ContextInterceptor
import dev.nestorperez.registrybackend.registry.RegistryApi
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SpringBootApplication
class RegistryBackendApplication {
    @Bean
    fun okHttpClient() = OkHttpClient
        .Builder()
        .addInterceptor(ContextInterceptor())
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

fun main(args: Array<String>) {
    runApplication<RegistryBackendApplication>(*args)
}
