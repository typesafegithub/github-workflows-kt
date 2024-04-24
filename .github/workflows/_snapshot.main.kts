#!/usr/bin/env kotlin
@file:Repository("file://~/.m2/repository/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.14.1-SNAPSHOT")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("gradle:actions__setup-gradle:v3")

import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.Workflow

fun Workflow.consistencyCheckPublishToMavenLocal() =
    copy(
        yamlConsistencyJobAdditionalSteps = {
            uses(
                name = "Set up JDK",
                action =
                    SetupJava(
                        javaVersion = "11",
                        distribution = SetupJava.Distribution.Zulu,
                    ),
            )
            uses(
                action =
                    ActionsSetupGradle(generateJobSummary = false),
            )
            run(
                name = "Publish to mavenLocal",
                command = "./gradlew publishToMavenLocal",
            )
        },
    )
