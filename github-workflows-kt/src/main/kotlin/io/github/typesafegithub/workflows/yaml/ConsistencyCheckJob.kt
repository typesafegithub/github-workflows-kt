package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.AbstractResult.Status
import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.dsl.WorkflowBuilder
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.internal.relativeToAbsolute
import java.nio.file.Path
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

@Suppress("LongMethod")
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
        val checkoutActionVersion =
            when (consistencyCheckJobConfig.checkoutActionVersion) {
                CheckoutActionVersionSource.BundledWithLibrary -> "v4"
                is CheckoutActionVersionSource.Given -> consistencyCheckJobConfig.checkoutActionVersion.version
                is CheckoutActionVersionSource.InferFromClasspath ->
                    inferCheckoutActionVersionFromClasspath(
                        consistencyCheckJobConfig.checkoutActionVersion.checkoutActionClassFQN,
                    )
            }

        uses(
            name = "Check out",
            // Since this action is used in a simple way, and we actually don't want to update the version
            // because it causes YAML regeneration, let's not use the type-safe binding here. It will also
            // let us avoid depending on a Maven-based action binding once bundled bindings are deprecated.
            action =
                CustomAction(
                    actionOwner = "actions",
                    actionName = "checkout",
                    actionVersion = checkoutActionVersion,
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
                name = "[Fallback] Start the local server",
                command = "docker run -p 8080:8080 krzema12/github-workflows-kt-jit-binding-server &",
                condition = ifFirstCompilationFails,
            )
            run(
                name = "[Fallback] Wait for the server",
                command =
                    "curl --head -X GET --retry 60 --retry-all-errors --retry-delay 1 " +
                        "http://localhost:8080/status",
                condition = ifFirstCompilationFails,
            )
            run(
                name = "[Fallback] Replace server URL in script",
                command =
                    "sed -i -e 's/https:\\/\\/bindings.krzeminski.it/http:\\/\\/localhost:8080/g' " +
                        sourceFilePath,
                condition = ifFirstCompilationFails,
            )
            run(
                name = "[Fallback] Execute script again",
                command = "rm -f '$targetFilePath' && '$sourceFilePath'",
                condition = ifFirstCompilationFails,
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
}

private fun inferCheckoutActionVersionFromClasspath(checkoutActionClassFQN: String): String {
    val clazz: Class<*> =
        try {
            Class.forName(checkoutActionClassFQN)
        } catch (_: ClassNotFoundException) {
            error(
                "actions/checkout is not found in the classpath! " +
                    "Either add a dependency on it (`@file:DependsOn(\"actions:checkout:<version>\")`), " +
                    "or don't use CheckoutActionVersionSource.InferFromClasspath()",
            )
        } as Class<*>
    // It's easier to call the primary constructor, even though it's private, because
    // the public constructor requires named arguments, and I'm not sure how to call
    // it with these.
    val constructor =
        clazz.kotlin.primaryConstructor!!.also {
            it.isAccessible = true
        }
    val args: Map<KParameter, Any?> = constructor.parameters.associateWith { null }
    val bindingObject = constructor.callBy(args) as RegularAction<*>
    return bindingObject.actionVersion
}
