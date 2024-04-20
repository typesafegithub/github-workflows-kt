#!/usr/bin/env kotlin

// usage:
// echo "" > summary.md && GITHUB_STEP_SUMMARY=summary.md GITHUB_TOKEN=$token ./outdated_workflow.main.kts

@file:Repository("https://repo1.maven.org/maven2/")
@file:Repository("file://~/.m2/repository/")
@file:DependsOn("io.github.typesafegithub:action-versions-updates:1.14.1-SNAPSHOT")
@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn(
    "actions:checkout:v3",
    "actions:setup-java:v3",
)
@file:DependsOn("actions:setup-node:v3")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.actions.SetupNode
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.updates.reportAvailableUpdates
import io.github.typesafegithub.workflows.yaml.writeToFile

val workflow =
    workflow(
        name = "This is a example of a horribly outdated workflow",
        on =
            listOf(
                PullRequest(),
                Push(branches = listOf("action-updates")),
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
                    name = "setup jdk",
                    action =
                        SetupJava(
                            javaPackage = SetupJava.JavaPackage.Jdk,
                            javaVersion = "21",
                            architecture = "x64",
                            distribution = SetupJava.Distribution.Adopt,
                            cache = SetupJava.BuildPlatform.Gradle,
                        ),
                )
                uses(
                    name = "setup nodejs",
                    action = SetupNode(),
                )
            }
    }

workflow.reportAvailableUpdates()
workflow.writeToFile(addConsistencyCheck = true)
