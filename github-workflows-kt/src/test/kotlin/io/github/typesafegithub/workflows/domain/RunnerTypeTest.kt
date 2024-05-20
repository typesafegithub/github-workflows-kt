package io.github.typesafegithub.workflows.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec

class RunnerTypeTest : FunSpec({
    context("Labelled") {
        test("should throw on invalid arguments") {
            shouldThrow<IllegalArgumentException> {
                RunnerType.Labelled()
            }
            shouldThrow<IllegalArgumentException> {
                RunnerType.Labelled(emptySet())
            }
        }
    }
})
