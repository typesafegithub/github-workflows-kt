package test

import io.github.typesafegithub.workflows.scriptgenerator.filename
import io.github.typesafegithub.workflows.scriptmodel.normalizeYaml
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.net.URL

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

    test("Normalize scalars") {
        val input = """
            name: Check build and run tests
            on: workflow_dispatch
        """.trimIndent()

        input.normalizeYaml() shouldBe """
           name: Check build and run tests
           on:
             workflow_dispatch: {}
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

    test("filename from URL") {
        val url =
            URL("https://raw.githubusercontent.com/jmfayard/refreshVersions/main/.github/workflows/publish-mkdocs-website.yml")
        url.filename() shouldBe "publish-mkdocs-website"

        val url2 =
            URL("https://raw.githubusercontent.com/jmfayard/refreshVersions/main/.github/workflows/publish-mkdocs-website.yaml")
        url2.filename() shouldBe "publish-mkdocs-website"
    }

    test("Normalize workflow and job concurrency") {
        val input = """
            name: Test workflow

            concurrency: workflow_staging_environment

            jobs:
              "test_job":
                concurrency: job_staging_environment
        """.trimIndent()

        input.normalizeYaml() shouldBe """
            name: Test workflow

            concurrency:
              group: workflow_staging_environment
              cancel-in-progress: false

            jobs:
              "test_job":
                concurrency:
                  group: job_staging_environment
                  cancel-in-progress: false
        """.trimIndent()
    }
})
