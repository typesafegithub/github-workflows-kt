package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.domain.triggers.Trigger
import io.github.typesafegithub.workflows.dsl.HasCustomArguments
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig
import kotlinx.serialization.Contextual
import java.io.File

public data class Workflow(
    val name: String,
    val on: List<Trigger>,
    val env: Map<String, String>,
    val sourceFile: File?,
    val targetFileName: String?,
    val consistencyCheckJobConfig: ConsistencyCheckJobConfig,
    val concurrency: Concurrency? = null,
    val permissions: Map<Permission, Mode>? = null,
    val jobs: List<Job<*>>,
    override val _customArguments: Map<String, @Contextual Any?> = mapOf(),
) : HasCustomArguments

/**
 * @see <a href="https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#permissions">GitHub</a>
 */
@Suppress("MaxLineLength")
public sealed class Permission(
    public val value: String,
) {
    public object Actions : Permission("actions")

    public object Checks : Permission("checks")

    public object Contents : Permission("contents")

    public object Deployments : Permission("deployments")

    public object Discussions : Permission("discussions")

    public object IdToken : Permission("id-token")

    public object Issues : Permission("issues")

    public object Packages : Permission("packages")

    public object Pages : Permission("pages")

    public object PullRequests : Permission("pull-requests")

    public object RepositoryProjects : Permission("repository-projects")

    public object SecurityEvents : Permission("security-events")

    public object Statuses : Permission("statuses")

    public data class Custom(
        val name: String,
    ) : Permission(value = name)
}

/**
 * @see <a href="https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#permissions">GitHub</a>
 */
@Suppress("MaxLineLength")
public sealed class Mode(
    public val value: String,
) {
    public object Read : Mode("read")

    public object Write : Mode("write")

    public object None : Mode("none")

    public data class Custom(
        val name: String,
    ) : Mode(value = name)
}
