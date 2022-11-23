package it.krzeminski.githubactions.dsl.expressions

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@Suppress("VariableNaming")
class ContextsTest : FunSpec({

    val dollar = '$'.toString()

    test("Environment variables") {
        assertSoftly {
            val DAY_OF_WEEK by Contexts.env
            "$DAY_OF_WEEK == 'Monday'" shouldBe "${dollar}DAY_OF_WEEK == 'Monday'"

            "${Contexts.env.CI} == true && ${Contexts.env.GITHUB_ACTIONS} == true" shouldBe
                "${dollar}CI == true && ${dollar}GITHUB_ACTIONS == true"
        }
    }

    test("Environment variables from expressions") {
        val GREETING by Contexts.env
        expr { GREETING } shouldBe "$dollar{{ GREETING }}"
        expr { env.GITHUB_ACTIONS } shouldBe "$dollar{{ GITHUB_ACTIONS }}"
    }

    test("Secrets") {
        assertSoftly {
            expr { secrets.GITHUB_TOKEN } shouldBe expr("secrets.GITHUB_TOKEN")

            val NPM_TOKEN by Contexts.secrets
            expr { NPM_TOKEN } shouldBe expr("secrets.NPM_TOKEN")
        }
    }

    test("Runner context") {
        assertSoftly {
            expr { runner.name } shouldBe expr("runner.name")
            expr { runner.os } shouldBe expr("runner.os")
            expr { runner.arch } shouldBe expr("runner.arch")
            expr { runner.temp } shouldBe expr("runner.temp")
            expr { runner.tool_cache } shouldBe expr("runner.tool_cache")
            expr { runner.workspace } shouldBe expr("runner.workspace")
        }
    }

    test("Functions") {
        assertSoftly {
            expr { always() } shouldBe expr("always()")
            expr { success() } shouldBe expr("success()")
            expr { always() } shouldBe expr("always()")
            expr { failure() } shouldBe expr("failure()")
            expr { cancelled() } shouldBe expr("cancelled()")
            expr { toJSON("job") } shouldBe expr("toJSON(job)")

            expr { contains("he", "hello", quote = true) } shouldBe
                expr("contains('he', 'hello')")

            expr { fromJSON("needs.job1.outputs.matrix") } shouldBe
                expr("fromJSON(needs.job1.outputs.matrix)")

            expr { format("Hello {0} {1} {2}", "Mona", "the", "Octocat", quote = true) } shouldBe
                expr("format('Hello {0} {1} {2}', 'Mona', 'the', 'Octocat')")

            expr { hashFiles("**/package-lock.json", "**/Gemfile.lock", quote = true) } shouldBe
                expr("hashFiles('**/package-lock.json', '**/Gemfile.lock')")

            expr { startsWith("Hello world", "He", quote = true) } shouldBe
                expr("startsWith('Hello world', 'He')")

            expr { endsWith("Hello world", "ld", quote = true) } shouldBe
                expr("endsWith('Hello world', 'ld')")

            expr { join("github.event.issue.labels.*.name", "', '") } shouldBe
                expr("join(github.event.issue.labels.*.name, ', ')")

            // Escape the single quotes in order to prevent having invalid JSONs
            expr {
                startsWith("Let's dance", "Let's dance and be happy!", quote = true)
            } shouldBe expr("startsWith('Let\\'s dance', 'Let\\'s dance and be happy!')")
        }
    }

    test("GitHub context") {
        assertSoftly {
            expr { github.token } shouldBe expr("github.token")
            expr { github.job } shouldBe expr("github.job")
            expr { github.sha } shouldBe expr("github.sha")
            expr { github.repository_owner } shouldBe expr("github.repository_owner")
            expr { github.repositoryUrl } shouldBe expr("github.repositoryUrl")
            expr { github.action } shouldBe expr("github.action")
        }
    }

    test("GitHub.event context") {
        assertSoftly {
            expr { github.eventPush.ref } shouldBe expr("github.event.ref")
            expr { github.eventPullRequest.pull_request.number } shouldBe expr("github.event.pull_request.number")
            expr { github.eventRelease.release.url } shouldBe expr("github.event.release.url")
            expr { github.eventWorkflowDispatch.workflow } shouldBe expr("github.event.workflow")
            expr { github.eventPush.repository.size } shouldBe expr("github.event.repository.size")
        }
    }
},)
