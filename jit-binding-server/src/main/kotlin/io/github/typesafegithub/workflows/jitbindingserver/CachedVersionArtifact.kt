package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.mavenbinding.VersionArtifacts

/**
 * This wrapper exists because Caffeine/Aedile cache doesn't support caching
 * null values.
 */
sealed interface CachedVersionArtifact {
    object Absent : CachedVersionArtifact

    class Present(
        val artifacts: VersionArtifacts,
    ) : CachedVersionArtifact
}

fun CachedVersionArtifact.toNullableVersionArtifacts(): VersionArtifacts? =
    when (this) {
        is CachedVersionArtifact.Absent -> null
        is CachedVersionArtifact.Present -> artifacts
    }

fun VersionArtifacts?.toCachedVersionArtifact(): CachedVersionArtifact =
    when (this) {
        null -> CachedVersionArtifact.Absent
        else -> CachedVersionArtifact.Present(this)
    }
