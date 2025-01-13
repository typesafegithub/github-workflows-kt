package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.dsl.WorkflowBuilder
import io.github.typesafegithub.workflows.internal.relativeToAbsolute
import java.nio.file.Path
import kotlin.io.path.invariantSeparatorsPathString

internal fun WorkflowBuilder.consistencyCheckJob(
    sourceFilePath: String?,
    targetFileName: String?,
    gitRootDir: Path?,
    consistencyCheckJobConfig: ConsistencyCheckJobConfig.Configuration,
): Job<JobOutputs.EMPTY> {
    check(gitRootDir != null && sourceFilePath != null) {
        "consistency check requires a valid sourceFile and Git root directory"
    }

    val targetFilePath =
        gitRootDir
            .resolve(".github")
            .resolve("workflows")
            .resolve(targetFileName)
            .relativeToAbsolute(gitRootDir)
            .invariantSeparatorsPathString

    return this.job(
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

        run(
            name = "Execute script",
            command = "rm '$targetFilePath' && '$sourceFilePath'",
        )
        run(
            name = "Consistency check",
            command = "git diff --exit-code '$targetFilePath'",
        )
    }
}
