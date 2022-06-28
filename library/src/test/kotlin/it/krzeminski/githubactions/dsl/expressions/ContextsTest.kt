package it.krzeminski.githubactions.dsl.expressions

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@Suppress("VariableNaming")
class ContextsTest : FunSpec({

    val dollar = '$'.toString()

    test("Environment variables") {
        assertSoftly {
            val DAY_OF_WEEK by Env
            "$DAY_OF_WEEK == 'Monday'" shouldBe "${dollar}DAY_OF_WEEK == 'Monday'"

            "${Env.CI} == true && ${Env.GITHUB_ACTIONS} == true" shouldBe
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
})
