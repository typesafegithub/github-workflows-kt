package io.github.typesafegithub.workflows.internal.model

import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.nio.file.Path

public data class VersionUpdateAvailable(
    val action: RegularAction<*>,
    val stepName: String,
    val file: Path? = null,
    val line: Int? = null,
    val newVersions: List<Version>,
) {
    public fun mavenCoordinatesFor(version: Version): String {
        return mavenCoordinatesForAction(action, version)
    }

    public companion object {
        public fun mavenCoordinatesForAction(
            action: RegularAction<*>,
            version: Version,
        ): String {
            val mavenGroupId = action.actionOwner.replace(":", "__")
            val mavenArtifactId = action.actionName.replace(":", "__")
            return "$mavenGroupId:$mavenArtifactId:${version.version}"
        }
    }
}
