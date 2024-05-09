package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.KotlinLogicStep
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.contexts.Contexts
import io.github.typesafegithub.workflows.domain.contexts.GithubContext
import io.github.typesafegithub.workflows.dsl.toBuilder
import io.github.typesafegithub.workflows.internal.relativeToAbsolute
import io.github.typesafegithub.workflows.yaml.Preamble.Just
import io.github.typesafegithub.workflows.yaml.Preamble.WithOriginalAfter
import io.github.typesafegithub.workflows.yaml.Preamble.WithOriginalBefore
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.invariantSeparatorsPathString

/**
 * Writes the workflow given in the receiver to a YAML string, under a path that is built this way:
 * `<git-repo-root>/.github/workflows/<[Workflow.targetFileName]>.yaml`.
 *
 * @receiver a workflow which needs to be written to the file.
 *
 * @param addConsistencyCheck If true, adds an extra job that makes sure the Kotlin script defined in
 * [Workflow.sourceFile] produces exactly the same YAML as in [Workflow.targetFileName], and fails the whole workflow if
 * it's not the case. This parameter defaults to `true` if [Workflow.sourceFile] is set, otherwise defaults to `false`.
 * @param gitRootDir Path to the git root directory, used for building relative paths for the consistency check. Usually
 * there's no need to set it explicitly, unless testing the library. Leave unset if unsure.
 * @param preamble Allows customizing the comment at the beginning of the generated YAML by either passing an extra
 * string, or replacing the whole preamble.
 */
internal fun Workflow.writeToFile(
    addConsistencyCheck: Boolean,
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

    checkNotNull(this.targetFileName) {
        "targetFileName must not be null"
    }

    val yaml =
        generateYaml(
            addConsistencyCheck = addConsistencyCheck,
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

@Suppress("LongMethod")
private fun Workflow.generateYaml(
    addConsistencyCheck: Boolean,
    gitRootDir: Path?,
    preamble: Preamble?,
): String {
    val sourceFilePath =
        gitRootDir?.let {
            sourceFile?.relativeToAbsolute(gitRootDir)?.invariantSeparatorsPathString
        }

    val jobsWithConsistencyCheck =
        if (addConsistencyCheck) {
            check(gitRootDir != null && sourceFile != null) {
                "consistency check requires a valid sourceFile and Git root directory"
            }

            checkNotNull(targetFileName) {
                "consistency check requires a targetFileName"
            }

            val targetFilePath =
                gitRootDir.resolve(".github").resolve("workflows").resolve(targetFileName)
                    .relativeToAbsolute(gitRootDir).invariantSeparatorsPathString

            val consistencyCheckJob =
                this.toBuilder().job(
                    id = "check_yaml_consistency",
                    name = "Check YAML consistency",
                    runsOn = UbuntuLatest,
                    condition = yamlConsistencyJobCondition,
                    env = yamlConsistencyJobEnv,
                ) {
                    uses(
                        name = "Check out",
                        // Since this action is used in a simple way, and we actually don't want to update the version
                        // because it causes YAML regeneration, let's not use the type-safe binding here. It will also
                        // let us avoid depending on a Maven-based action binding once bundled bindings are deprecated.
                        action =
                            CustomAction(
                                actionOwner = "actions",
                                actionName = "checkout",
                                actionVersion = "v4",
                            ),
                    )

                    yamlConsistencyJobAdditionalSteps?.also { block ->
                        block()
                    }

                    run(
                        name = "Execute script",
                        command =
                            "rm '$targetFilePath' " +
                                "&& '$sourceFilePath'",
                    )
                    run(
                        name = "Consistency check",
                        command = "git diff --exit-code '$targetFilePath'",
                    )
                }
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
