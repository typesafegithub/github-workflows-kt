package it.krzeminski.githubactions.dsl.expressions

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ContextsTest : FunSpec({

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
