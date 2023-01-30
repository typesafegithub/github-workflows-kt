package it.krzeminski.githubactions.yaml

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow

class StandaloneWorkflowTest : DescribeSpec({
    val workflow = workflow(
        name = "test",
        on = listOf(Push()),
    ) {
        job("test", runsOn = RunnerType.UbuntuLatest) {
            run(command = "echo 'Hello!'")
        }
    }

    it("should allow toYaml without sourceFile") {
        val yaml = workflow.toYaml()

        yaml shouldBe """
            # This file was generated using a Kotlin DSL.
            # If you want to modify the workflow, please change the Kotlin source and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-workflows-kt

            name: test
            on:
              push: {}
            jobs:
              test:
                runs-on: ubuntu-latest
                steps:
                - id: step-0
                  run: echo 'Hello!'

        """.trimIndent()
    }

    it("should fail on addConsistencyCheck when sourceFile is absent") {
        shouldThrow<IllegalStateException> {
            workflow.toYaml(addConsistencyCheck = true)
        }
    }
},)
