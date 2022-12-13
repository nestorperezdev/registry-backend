package dev.nestorperez.registrybackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RegistryBackendApplication

fun main(args: Array<String>) {
	runApplication<RegistryBackendApplication>(*args)
}
