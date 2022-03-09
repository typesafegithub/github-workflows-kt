package it.krzeminski.githubactions.yaml

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.withFreeArgs
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

class FreeYamlArgsTest : FunSpec({
    test("Action with free arguments") {
        val action = CheckoutV2(
            repository = "repository",
            ref = "main",
        ).withFreeArgs(
            "hello" to "world",
            "repository" to "jcenter",
            "groceries" to listOf("cheese", "wine"),
        )

        val step = ExternalActionStep(
            id = "someId",
            name = "Some external action",
            action = action,
        )
        listOf(step).stepsToYaml() shouldBe """
            - id: someId
              name: Some external action
              uses: actions/checkout@v2
              with:
                repository: jcenter
                ref: main
                hello: world
                groceries:
                  - 'cheese'
                  - 'wine'
        """.trimIndent()
    }

    test("Triggers with free yaml args") {
        val triggers: List<Trigger> = listOf(
            PullRequest().withFreeArgs(
                "types" to listOf("ignored"),
                "lang" to "french",
                "list" to listOf(1, 2, 3)
            ),
            WorkflowDispatch().withFreeArgs(
                "lang" to "french",
                "list" to listOf(1, 2, 3)
            ),
            Push().withFreeArgs(
                "branches" to listOf("main", "master"),
                "tags" to listOf("tag1", "tag2")
            ),
            Schedule(emptyList()).withFreeArgs(
                "cron" to Cron(hour = "7", minute = "0").expression
            ),
            PullRequestTarget().withFreeArgs(
                "branches" to listOf("main", "master"),
                "tags" to listOf("tag1", "tag2")
            ),
        )
        triggers.triggersToYaml() shouldBe """
            pull_request:
              types:
                - 'ignored'
              lang: french
              list:
                - '1'
                - '2'
                - '3'
            workflow_dispatch:
              lang: french
              list:
                - '1'
                - '2'
                - '3'
            push:
              branches:
                - 'main'
                - 'master'
              tags:
                - 'tag1'
                - 'tag2'
            schedule:
              cron: 0 7 * * *
            pull_request_target:
              branches:
                - 'main'
                - 'master'
              tags:
                - 'tag1'
                - 'tag2'
        """.trimIndent()
    }

    test("Workflow and Job with free yaml args") {
        val workflow = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
            targetFile = Paths.get(".github/workflows/some_workflow.yaml"),
        ) {
            job(
                name = "test_job",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                run(
                    name = "Hello world!",
                    command = "echo 'hello!'",
                )
            }.withFreeArgs(
                "distribute-job" to true,
                "servers" to listOf("server-1", "server-2")
            )
        }
        workflow.withFreeArgs(
            "dry-run" to true,
            "written-by" to listOf("Alice", "Bob")
        )
        workflow.toYaml(addConsistencyCheck = false) shouldBe """
          # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
          # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
          # Generated with https://github.com/krzema12/github-actions-kotlin-dsl
          
          name: Test workflow
          
          on:
            push:
          
          jobs:
            "test_job":
              runs-on: "ubuntu-latest"
              steps:
                - id: step-0
                  name: Hello world!
                  run: echo 'hello!'
              distribute-job: true
              servers:
                - 'server-1'
                - 'server-2'
          dry-run: true
          written-by:
            - 'Alice'
            - 'Bob'
        """.trimIndent()
    }
})
