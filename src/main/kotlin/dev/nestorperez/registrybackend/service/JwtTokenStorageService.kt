package dev.nestorperez.registrybackend.service

import dev.nestorperez.registrybackend.schema.DockerIoAuthSettings
import org.springframework.stereotype.Component

/**
 * Service to store and retrieve the token for the Docker Registry
 */
@Component
class JwtTokenStorageService {
    /**
     * Map to save hashcode of the DockerRegistryIOTokenKey and the token
     */
    private val map = mutableMapOf<Int, String>()

    /**
     * Given a DockerIoAuthSettings, it returns the token if it exists
     */
    fun getToken(key: DockerIoAuthSettings): String? {
        return map[key.hashCode()]
    }

    /**
     * Given a DockerIoAuthSettings and a token, it saves the token
     */
    fun saveToken(key: DockerIoAuthSettings, token: String) {
        map[key.hashCode()] = token
    }
}