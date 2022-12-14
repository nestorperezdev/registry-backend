package dev.nestorperez.registrybackend.util

import dev.nestorperez.registrybackend.model.AppVersion
import dev.nestorperez.registrybackend.model.SemanticVersion
import org.springframework.core.SpringVersion
import org.springframework.core.env.Environment

fun parseAppVersion(env: Environment) = SpringVersion.getVersion()?.let { version ->
    version.split("-").let { buildParts ->
        buildParts[0].split(".").let { semanticParts ->
            AppVersion(
                version = SemanticVersion(
                    major = semanticParts[0].toInt(),
                    minor = semanticParts[1].toInt(),
                    patch = semanticParts[2].toInt()
                ), build = env.activeProfiles.joinToString(",")
            )
        }
    }
}