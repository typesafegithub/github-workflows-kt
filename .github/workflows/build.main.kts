#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.12.0")
@file:Import("_shared.main.kts")
@file:Import("setup-java.main.kts")
@file:Import("setup-python.main.kts")
@file:Import("generated/actions/checkout.kt")
@file:Import("generated/actions/setup-java.kt")
@file:Import("generated/gradle/actions/setup-gradle.kt")

import io.github.typesafegithub.workflows.annotations.ExperimentalClientSideBindings
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.RunnerType.Windows2022
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

val GRADLE_ENCRYPTION_KEY by Contexts.secrets

@OptIn(ExperimentalClientSideBindings::class)
workflow(
    name = "Build",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    listOf(UbuntuLatest, Windows2022).forEach { runnerType ->
        job(
            id = "build-for-${runnerType::class.simpleName}",
            runsOn = runnerType,
        ) {
            uses(action = Checkout())
            setupJava()
            uses(action = ActionsSetupGradle(
                cacheEncryptionKey = expr { GRADLE_ENCRYPTION_KEY },
            ))
            run(
                name = "Build",
                command = "./gradlew build",
            )
        }
    }

    job(
        id = "publish-snapshot",
        name = "Publish snapshot",
        runsOn = UbuntuLatest,
        condition = expr { "${github.ref} == 'refs/heads/main'" },
        env = linkedMapOf(
            "SIGNING_KEY" to expr("secrets.SIGNING_KEY"),
            "SIGNING_PASSWORD" to expr("secrets.SIGNING_PASSWORD"),
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
                command = "./gradlew $library:publishToSonatype closeAndReleaseSonatypeStagingRepository --no-configuration-cache",
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
            name = "Generate action bindings",
            command = ".github/workflows/generate-action-bindings.main.kts",
        )
        run(
            command = """
            find -name *.main.kts -print0 | while read -d ${'$'}'\0' file
            do
                echo "Compiling ${'$'}file..."
                kotlinc -Werror -Xallow-any-scripts-in-source-roots "${'$'}file"
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
        uses(
            name = "Set up Java in proper version",
            action = SetupJava(
                javaVersion = "17",
                distribution = SetupJava.Distribution.Zulu,
                cache = SetupJava.BuildPlatform.Gradle,
            ),
        )
        run(command = "cd .github/workflows")
        run(
            name = "Generate action bindings",
            command = ".github/workflows/generate-action-bindings.main.kts",
        )
        run(
            name = "Regenerate all workflow YAMLs",
            command = """
            find -name "*.main.kts" -print0 | while read -d ${'$'}'\0' file
            do
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
}.writeToFile(generateActionBindings = true)
