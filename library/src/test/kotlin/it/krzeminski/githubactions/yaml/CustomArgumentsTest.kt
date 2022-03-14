package it.krzeminski.githubactions.yaml

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.BooleanCustomValue
import it.krzeminski.githubactions.dsl.IntCustomValue
import it.krzeminski.githubactions.dsl.ListCustomValue
import it.krzeminski.githubactions.dsl.ObjectCustomValue
import it.krzeminski.githubactions.dsl.StringCustomValue
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

class CustomArgumentsTest : FunSpec({

    test("Triggers with free yaml args") {
        val triggers: List<Trigger> = listOf(
            PullRequest(
                _customArguments = mapOf(
                    "types" to ListCustomValue("ignored"),
                    "lang" to StringCustomValue("french"),
                    "fast" to BooleanCustomValue(true),
                    "answer" to IntCustomValue(42),
                    "list" to ListCustomValue(1, 2, 3),
                ),
            ),
            WorkflowDispatch(
                _customArguments = mapOf(
                    "lang" to StringCustomValue("french"),
                    "list" to ListCustomValue(1, 2, 3),
                ),
            ),
            Push(
                _customArguments = mapOf(
                    "branches" to ListCustomValue("main", "master"),
                    "tags" to ListCustomValue("tag1", "tag2"),
                ),
            ),
            Schedule(
                triggers = emptyList(),
                _customArguments = mapOf(
                    "cron" to StringCustomValue("0 7 * * *"),
                    "object" to ObjectCustomValue(
                        mapOf(
                            "some-property" to "good",
                            "other-property" to "better",
                        ),
                    )
                ),
            ),
            PullRequestTarget(
                _customArguments = mapOf(
                    "branches" to ListCustomValue("main", "master"),
                    "tags" to ListCustomValue("tag1", "tag2"),
                ),
            ),
        )
        triggers.triggersToYaml() shouldBe """
            pull_request:
              types:
                - 'ignored'
              lang: french
              fast: true
              answer: 42
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
              object:
                some-property: good
                other-property: better
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
            _customArguments = mapOf(
                "dry-run" to BooleanCustomValue(true),
                "written-by" to ListCustomValue("Alice", "Bob"),
            )
        ) {
            job(
                name = "test_job",
                runsOn = RunnerType.UbuntuLatest,
                _customArguments = mapOf(
                    "distribute-job" to BooleanCustomValue(true),
                    "servers" to ListCustomValue("server-1", "server-2")
                ),
            ) {
                run(
                    name = "Hello world!",
                    command = "echo 'hello!'",
                )
            }
        }
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
