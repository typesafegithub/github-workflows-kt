#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:Repository("file://~/.m2/repository/")
@file:DependsOn("io.github.typesafegithub:action-versions-updates:1.14.1-SNAPSHOT")
@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn("actions:checkout:v3")
@file:Import("_snapshot.main.kts")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

val workflow =
    workflow(
        name = "Test workflow",
        on =
            listOf(
                Push(branches = listOf("main")),
                PullRequest(),
            ),
        sourceFile = __FILE__.toPath(),
    ) {
        job(
            id = "test_job",
            name = "Test Job",
            runsOn = RunnerType.UbuntuLatest,
        ) {
            uses(action = CheckoutV4())
            run(command = "echo 'hello!'")
        }
    }
        .publishToMavenLocal()
        .writeToFile()
