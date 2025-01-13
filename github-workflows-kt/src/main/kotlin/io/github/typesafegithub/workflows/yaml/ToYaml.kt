package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.AbstractResult.Status
import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.KotlinLogicStep
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.contexts.Contexts
import io.github.typesafegithub.workflows.domain.contexts.GithubContext
import io.github.typesafegithub.workflows.dsl.expressions.expr
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
            check(gitRootDir != null && sourceFile != null) {
                "consistency check requires a valid sourceFile and Git root directory"
            }

            val targetFilePath =
                gitRootDir
                    .resolve(".github")
                    .resolve("workflows")
                    .resolve(targetFileName)
                    .relativeToAbsolute(gitRootDir)
                    .invariantSeparatorsPathString

            val consistencyCheckJob =
                this.toBuilder().job(
                    id = "check_yaml_consistency",
                    name = "Check YAML consistency",
                    runsOn = UbuntuLatest,
                    condition = consistencyCheckJobConfig.condition,
                    env = consistencyCheckJobConfig.env,
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

                    consistencyCheckJobConfig.additionalSteps?.also { block ->
                        block()
                    }

                    if (consistencyCheckJobConfig.useLocalBindingsServerAsFallback) {
                        val firstCompilationStep =
                            run(
                                name = "Execute script",
                                command = "rm '$targetFilePath' && '$sourceFilePath'",
                                continueOnError = true,
                            )
                        val ifFirstCompilationFails = expr { firstCompilationStep.outcome.neq(Status.Success) }
                        run(
                            name = "Start the local server",
                            command = "docker run -p 8080:8080 krzema12/github-workflows-kt-jit-binding-server &",
                            condition = ifFirstCompilationFails,
                        )
                        run(
                            name = "Wait for the server",
                            command =
                                "curl --head -X GET --retry 60 --retry-all-errors --retry-delay 1 " +
                                    "http://localhost:8080/status",
                            condition = ifFirstCompilationFails,
                        )
                        run(
                            name = "Replace server URL in script",
                            command =
                                "sed -i -e 's/https:\\/\\/bindings.krzeminski.it/http:\\/\\/localhost:8080/g' " +
                                    "$sourceFilePath",
                            condition = ifFirstCompilationFails,
                        )
                        run(
                            name = "Execute script again",
                            command = "rm -f '$targetFilePath' && '$sourceFilePath'",
                        )
                    } else {
                        run(
                            name = "Execute script",
                            command = "rm '$targetFilePath' && '$sourceFilePath'",
                        )
                    }

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
