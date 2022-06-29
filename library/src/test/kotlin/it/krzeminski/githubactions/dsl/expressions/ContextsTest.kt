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
        }
    }
})
