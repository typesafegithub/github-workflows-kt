package test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.scriptmodel.normalizeYaml

class NormalizeYamlTest : FunSpec({

    test("Add empty object to prevent the trigger to be null") {
        val input = """
                name: triggers
                on:
                    check_run:
                        types:
                    check_suite:
                    create: {}
                    delete:
                    deployment:
                jobs:
                    "a":
        """.trimIndent()

        input.normalizeYaml() shouldBe """
                name: triggers
                on:
                    check_run:
                        types:
                    check_suite: {}
                    create: {}
                    delete: {}
                    deployment: {}
                jobs:
                    "a":
        """.trimIndent()
    }

    test("Convert triggers from List<String> to object") {
        val input = """
                name: triggers
                on: [push, pull_request]
                on: ['push', 'pull_request']
                on: ["push", "pull_request"]
                jobs:
                    "a":
        """.trimIndent()

        input.normalizeYaml() shouldBe """
            name: triggers
            on:
              push: {}
              pull_request: {}
            on:
              push: {}
              pull_request: {}
            on:
              push: {}
              pull_request: {}
            jobs:
                "a":
        """.trimIndent()
    }

    test("Do not modify trigger with a list") {
        val input = """
            on:
              schedule:
               - cron: '0 0 * * *'
        """.trimIndent()

        input.normalizeYaml() shouldBe input
    }
})
