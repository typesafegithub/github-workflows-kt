#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.25.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3.Distribution.Zulu
import it.krzeminski.githubactions.actions.actions.SetupPythonV4
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.RunnerType.Windows2022
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import it.krzeminski.githubactions.yaml.writeToFile

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
            id = "build-for-${runnerType.toYaml()}",
            runsOn = runnerType,
        ) {
            uses(CheckoutV3())
            uses(
                name = "Set up JDK",
                action = SetupJavaV3(
                    javaVersion = "17",
                    distribution = Zulu,
                )
            )
            uses(
                name = "Build",
                action = GradleBuildActionV2(
                    arguments = "build",
                )
            )
        }
    }

    job(
        id = "build_docs",
        name = "Build docs",
        runsOn = UbuntuLatest,
    ) {
        uses(CheckoutV3())
        uses(SetupPythonV4(pythonVersion = "3.8"))
        run("pip install -r docs/requirements.txt")
        run("mkdocs build --site-dir public")
    }

    job(
        id = "build_kotlin_scripts",
        name = "Build Kotlin scripts",
        runsOn = UbuntuLatest,
    ) {
        uses(CheckoutV3())
        run("find -name '*.main.kts' | xargs kotlinc")
    }
}.writeToFile()
