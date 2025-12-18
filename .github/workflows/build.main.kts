#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.7.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:cache:v5")
@file:DependsOn("actions:checkout:v6")
@file:DependsOn("actions:setup-java:v5")
@file:DependsOn("gradle:actions__setup-gradle:v5")

@file:Import("_shared.main.kts")
@file:Import("setup-java.main.kts")
@file:Import("setup-python.main.kts")

import io.github.typesafegithub.workflows.actions.actions.Cache
import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.RunnerType.WindowsLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.CheckoutActionVersionSource
import io.github.typesafegithub.workflows.yaml.DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG

val GRADLE_ENCRYPTION_KEY by Contexts.secrets

workflow(
    name = "Build",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    consistencyCheckJobConfig = DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG.copy(
        useLocalBindingsServerAsFallback = true,
        checkoutActionVersion = CheckoutActionVersionSource.InferFromClasspath(),
    ),
    sourceFile = __FILE__,
) {
    listOf(UbuntuLatest, WindowsLatest).forEach { runnerType ->
        job(
            id = "build-for-${runnerType::class.simpleName}",
            runsOn = runnerType,
        ) {
            uses(action = Checkout())
            // Workaround for https://github.com/gradle/gradle/issues/21265
            uses(action = Cache(
                path = listOf("buildSrc/build"),
                key = "gradle-buildSrc-build-dir-" + expr { runner.os },
            ))
            setupJava()
            uses(action = ActionsSetupGradle(
                cacheEncryptionKey = expr { GRADLE_ENCRYPTION_KEY },
            ))
            run(
                name = "Build",
                command = "./gradlew build",
                env =
                    mapOf(
                        "GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN"),
                    ),
            )
        }
    }

    job(
        id = "publish-snapshot",
        name = "Publish snapshot",
        runsOn = UbuntuLatest,
        condition = expr { "${github.ref} == 'refs/heads/main'" },
        env = mapOf(
            "ORG_GRADLE_PROJECT_sonatypeUsername" to expr("secrets.ORG_GRADLE_PROJECT_SONATYPEUSERNAME"),
            "ORG_GRADLE_PROJECT_sonatypePassword" to expr("secrets.ORG_GRADLE_PROJECT_SONATYPEPASSWORD"),
        ),
    ) {
        uses(action = Checkout())
        setupJava()
        uses(action = ActionsSetupGradle())
        val setIsSnapshotVersionFlag = run(
            name = "Check if snapshot version is set",
            command = "./gradlew setIsSnapshotFlagInGithubOutput",
        )

        libraries.forEach { library ->
            run(
                name = "Publish '$library' to Sonatype",
                condition = expr("steps.${setIsSnapshotVersionFlag.id}.outputs.is-snapshot == 'true'"),
                command = "./gradlew $library:publishToSonatype --no-configuration-cache",
            )
        }
    }

    job(
        id = "build_docs",
        name = "Build docs",
        runsOn = UbuntuLatest,
    ) {
        uses(action = Checkout())
        setupPython()
        run(command = "pip install -r docs/requirements.txt")
        run(command = "mkdocs build --site-dir public")
    }


    job(
        id = "build_kotlin_scripts",
        name = "Build Kotlin scripts",
        runsOn = UbuntuLatest,
    ) {
        uses(action = Checkout())
        run(
            command = """
            find -name *.main.kts -print0 | while read -d ${'$'}'\0' file
            do
                # This script requires extra steps before it can be compiled, i.e. publishing
                # the library to Maven Local. It's handled by the consistency check in this workflow.
                if [ "${'$'}file" = "./.github/workflows/end-to-end-tests.main.kts" ]; then
                    continue
                fi

                echo "Compiling ${'$'}file..."
                kotlinc -Xallow-any-scripts-in-source-roots -Xuse-fir-lt=false "${'$'}file"
            done
            """.trimIndent()
        )
    }


    job(
        id = "workflows_consistency_check",
        name = "Run consistency check on all GitHub workflows",
        runsOn = UbuntuLatest,
    ) {
        uses(action = Checkout())
        setupJava()
        run(command = "cd .github/workflows")
        run(
            name = "Regenerate all workflow YAMLs",
            command = """
            find -name "*.main.kts" -print0 | while read -d ${'$'}'\0' file
            do
                # This script requires extra steps before it can be compiled, i.e. publishing
                # the library to Maven Local. It's handled by the consistency check in this workflow.
                if [ "${'$'}file" = "./.github/workflows/end-to-end-tests.main.kts" ]; then
                    continue
                fi

                if [ -x "${'$'}file" ]; then
                    echo "Regenerating ${'$'}file..."
                    (${'$'}file)
                fi
            done
            """.trimIndent(),
        )
        run(
            name = "Check if some file is different after regeneration",
            command = "git diff --exit-code .",
        )
    }
}
