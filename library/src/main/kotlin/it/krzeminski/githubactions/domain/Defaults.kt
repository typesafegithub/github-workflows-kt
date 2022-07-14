package it.krzeminski.githubactions.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Defaults(
    val run: Run,
)

@Serializable
data class Run(
    val shell: String? = null,
    @SerialName("working-directory")
    val workingDirectory: String? = null
)
