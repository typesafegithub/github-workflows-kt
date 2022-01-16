package it.krzeminski.githubactions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.Checkout
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Trigger
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml

class EndToEndTest : FunSpec({
    test("'hello world' workflow") {
        // Given
        val workflow = workflow(
            name = "Test workflow",
            on = listOf(Trigger.Push),
        ) {
            job(
                name = "test_job",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                uses(
                    name = "Check out",
                    action = Checkout(),
                )

                run(
                    name = "Hello world!",
                    command = "echo 'hello!'",
                )
            }
        }

        // when
        val actualYaml = workflow.toYaml()

        // then
        actualYaml shouldBe """
            name: "Test workflow"
            on:
              push: {}
            jobs:
              "test_job":
                runs-on: "ubuntu-latest"
                steps:
                - 
                  uses: "actions/checkout@v2"
                  with:
                    
                    fetch-depth: 1
                - 
                  name: "Hello world!"
                  run: "echo 'hello!'"
                needs: []
                strategy: {}
        """.trimIndent()
    }
})
