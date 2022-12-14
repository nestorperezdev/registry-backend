package dev.nestorperez.registrybackend.model

data class SemanticVersion(val major: Int, val minor: Int, val patch: Int)

data class AppVersion(val version: SemanticVersion, val build: String)
