package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.KotlinLogicStep
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.contexts.Contexts
import io.github.typesafegithub.workflows.domain.contexts.GithubContext
import io.github.typesafegithub.workflows.dsl.toBuilder
import io.github.typesafegithub.workflows.internal.relativeToAbsolute
import io.github.typesafegithub.workflows.shared.internal.findGitRoot
import io.github.typesafegithub.workflows.yaml.Preamble.Just
import io.github.typesafegithub.workflows.yaml.Preamble.WithOriginalAfter
import io.github.typesafegithub.workflows.yaml.Preamble.WithOriginalBefore
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.invariantSeparatorsPathString

/**
 * Writes the workflow given in the receiver to a YAML string, under a path that is built this way:
 * `<git-repo-root>/.github/workflows/<[Workflow.targetFileName]>.yaml`.
 *
 * @receiver a workflow which needs to be written to the file.
 *
 * @param gitRootDir Path to the git root directory, used for building relative paths for the consistency check. Usually
 * there's no need to set it explicitly, unless testing the library. Leave unset if unsure.
 * @param preamble Allows customizing the comment at the beginning of the generated YAML by either passing an extra
 * string, or replacing the whole preamble.
 */
internal fun Workflow.writeToFile(
    gitRootDir: Path?,
    preamble: Preamble?,
    getenv: (String) -> String?,
) {
    val runStepEnvVar = getenv("GHWKT_RUN_STEP")

    if (runStepEnvVar != null) {
        val (jobId, stepId) = runStepEnvVar.split(":")
        val kotlinLogicStep =
            this.jobs
                .first { it.id == jobId }
                .steps
                .first { it.id == stepId } as KotlinLogicStep
        val contexts = loadContextsFromEnvVars(getenv)
        kotlinLogicStep.logic(contexts)
        return
    }

    checkNotNull(gitRootDir) {
        "gitRootDir must be specified explicitly when sourceFile is null"
    }

    val yaml =
        generateYaml(
            gitRootDir = gitRootDir,
            preamble,
        )

    gitRootDir.resolve(".github").resolve("workflows").resolve(targetFileName).toFile().let {
        it.parentFile.mkdirs()
        it.writeText(yaml)
    }
}

private fun loadContextsFromEnvVars(getenv: (String) -> String?): Contexts {
    fun getEnvVarOrFail(varName: String): String = getenv(varName) ?: error("$varName should be set!")

    val githubContextRaw = getEnvVarOrFail("GHWKT_GITHUB_CONTEXT_JSON")
    val githubContext = json.decodeFromString<GithubContext>(githubContextRaw)
    return Contexts(
        github = githubContext,
    )
}

private fun commentify(preamble: String): String {
    if (preamble.isEmpty()) return ""

    return preamble
        .lineSequence()
        .joinToString("\n", postfix = "\n\n") { "# $it".trimEnd() }
}

/**
 * Return a YAML for a workflow passed as the receiver.
 */
@Suppress("LongMethod")
public fun Workflow.generateYaml(
    gitRootDir: Path? = sourceFile?.toPath()?.absolute()?.findGitRoot(),
    preamble: Preamble? = null,
): String {
    val sourceFilePath =
        gitRootDir?.let {
            sourceFile?.toPath()?.relativeToAbsolute(gitRootDir)?.invariantSeparatorsPathString
        }

    require(!(consistencyCheckJobConfig is ConsistencyCheckJobConfig.Configuration && targetFileName == null)) {
        "consistency check requires a targetFileName"
    }

    val jobsWithConsistencyCheck =
        if (consistencyCheckJobConfig is ConsistencyCheckJobConfig.Configuration) {
            val consistencyCheckJob =
                this.toBuilder().consistencyCheckJob(
                    sourceFilePath = sourceFilePath,
                    targetFileName = targetFileName,
                    gitRootDir = gitRootDir,
                    consistencyCheckJobConfig = consistencyCheckJobConfig,
                )
            listOf(consistencyCheckJob) +
                jobs.map {
                    it.copy(needs = it.needs + consistencyCheckJob)
                }
        } else {
            jobs
        }

    val originalPreamble =
        commentify(
            if (sourceFilePath != null) {
                """
                This file was generated using Kotlin DSL ($sourceFilePath).
                If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
                Generated with https://github.com/typesafegithub/github-workflows-kt
                """.trimIndent()
            } else {
                """
                This file was generated using a Kotlin DSL.
                If you want to modify the workflow, please change the Kotlin source and regenerate this YAML file.
                Generated with https://github.com/typesafegithub/github-workflows-kt
                """.trimIndent()
            },
        )

    val computedPreamble =
        when (preamble) {
            is Just -> commentify(preamble.content)
            is WithOriginalAfter -> commentify(preamble.content) + originalPreamble
            is WithOriginalBefore -> originalPreamble + commentify(preamble.content)
            null -> originalPreamble
        }

    val workflowToBeSerialized = this.toYamlInternal(jobsWithConsistencyCheck)
    val workflowAsYaml = workflowToBeSerialized.toYaml()

    return computedPreamble + workflowAsYaml
}

internal fun Map<Permission, Mode>.mapToYaml() = map { (p, m) -> p.value to m.value }.toMap()

@Suppress("SpreadOperator")
private fun Workflow.toYamlInternal(jobsWithConsistencyCheck: List<Job<*>>): Map<String, Any> =
    mapOfNotNullValues(
        "name" to name,
        "on" to on.triggersToYaml(),
        "permissions" to permissions?.mapToYaml(),
        "concurrency" to
            concurrency?.let {
                mapOf(
                    "group" to it.group,
                    "cancel-in-progress" to it.cancelInProgress,
                )
            },
        "env" to env.ifEmpty { null },
        *_customArguments.toList().toTypedArray(),
        "jobs" to jobsWithConsistencyCheck.jobsToYaml(),
    )

private val json = Json { ignoreUnknownKeys = true }
