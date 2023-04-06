package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.domain.triggers.Trigger
import io.github.typesafegithub.workflows.dsl.HasCustomArguments
import kotlinx.serialization.Contextual
import java.nio.file.Path

public data class Workflow(
    val name: String,
    val on: List<Trigger>,
    val env: LinkedHashMap<String, String>,
    val sourceFile: Path?,
    val targetFileName: String?,
    val concurrency: Concurrency? = null,
    val permissions: Collection<Permission>? = null,
    val yamlConsistencyJobCondition: String? = null,
    val jobs: List<Job<*>>,
    override val _customArguments: Map<String, @Contextual Any?> = mapOf(),
) : HasCustomArguments

public data class Permission internal constructor(public val type: String, public val mode: Mode) {
    public companion object {
        public fun of(value: String): PermissionBuilder = PermissionBuilder(value)
        public val actions: PermissionBuilder = of("actions")
        public val checks: PermissionBuilder = of("checks")
        public val contents: PermissionBuilder = of("contents")
        public val deployments: PermissionBuilder = of("deployments")
        public val idToken: PermissionBuilder = of("id-token")
        public val issues: PermissionBuilder = of("issues")
        public val discussions: PermissionBuilder = of("discussions")
        public val packages: PermissionBuilder = of("packages")
        public val pages: PermissionBuilder = of("pages")
        public val pullRequests: PermissionBuilder = of("pull-requests")
        public val repositoryProjects: PermissionBuilder = of("repository-projects")
        public val securityEvents: PermissionBuilder = of("security-events")
        public val statuses: PermissionBuilder = of("statuses")
    }
}

public enum class Mode {
    READ,
    WRITE,
    NONE,
    ;

    override fun toString(): String = name.lowercase()
}

public data class PermissionBuilder(val value: String) {
    public var read: Permission = Permission(value, Mode.READ)
    public var write: Permission = Permission(value, Mode.WRITE)
    public var mode: Permission = Permission(value, Mode.NONE)
}
