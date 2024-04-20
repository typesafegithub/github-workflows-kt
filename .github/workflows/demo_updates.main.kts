#!/usr/bin/env kotlin

// usage:
// echo "" > summary.md && GITHUB_STEP_SUMMARY=summary.md GITHUB_TOKEN=$token ./demo_updates.main.kts

@file:Repository("https://repo1.maven.org/maven2/")
@file:Repository("file://~/.m2/repository/")
@file:DependsOn("io.github.typesafegithub:action-versions-updates:1.14.1-SNAPSHOT")
@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn(
    "actions:checkout:v3",
    "actions:setup-java:v3",
)
@file:DependsOn("gradle:actions__setup-gradle:v3")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.shared.internal.findGitRoot
import io.github.typesafegithub.workflows.updates.reportAvailableUpdates
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.io.File
import kotlin.io.path.relativeTo

val workflow =
    workflow(
        name = "demo version updates",
        on =
            listOf(
                PullRequest(),
            ),
        sourceFile = __FILE__.toPath(),
    ) {
        val buildJob =
            job(
                id = "build",
                runsOn = RunnerType.UbuntuLatest,
                timeoutMinutes = 30,
            ) {
                uses(
                    name = "checkout",
                    action = Checkout(),
                )
                uses(
                    name = "Set up JDK",
                    action =
                        SetupJava(
                            javaVersion = "11",
                            distribution = SetupJava.Distribution.Zulu,
                        ),
                )
                uses(action = ActionsSetupGradle())
                run(
                    name = "Publish to mavenLocal",
                    command = "./gradlew publishToMavenLocal",
                )
                val currentFile = __FILE__.toPath().relativeTo(File(".").toPath().findGitRoot())
                run(
                    name = "execute script with github token",
                    command =
                        """
                        '$currentFile'
                        """.trimIndent(),
                    env =
                        linkedMapOf(
                            "GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN"),
                        ),
                )
            }
    }

workflow.reportAvailableUpdates()
workflow.writeToFile(addConsistencyCheck = false)
