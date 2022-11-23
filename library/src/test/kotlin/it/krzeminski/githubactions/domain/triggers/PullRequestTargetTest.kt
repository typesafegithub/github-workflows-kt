package it.krzeminski.githubactions.domain.triggers

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PullRequestTargetTest : FunSpec({
    context("validation errors") {
        test("both 'branches' and 'branchesIgnore' defined") {
            val exception = shouldThrow<IllegalArgumentException> {
                PullRequestTarget(
                    branches = listOf("branch1"),
                    branchesIgnore = listOf("branch2"),
                )
            }
            exception.message shouldBe "Cannot define both 'branches' and 'branchesIgnore'!"
        }

        test("both 'paths' and 'pathsIgnore' defined") {
            val exception = shouldThrow<IllegalArgumentException> {
                PullRequestTarget(
                    paths = listOf("path1"),
                    pathsIgnore = listOf("path2"),
                )
            }
            exception.message shouldBe "Cannot define both 'paths' and 'pathsIgnore'!"
        }
    }
},)
