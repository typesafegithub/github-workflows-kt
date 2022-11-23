package it.krzeminski.githubactions.domain.triggers

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PushTest : FunSpec({
    context("validation errors") {
        test("both 'branches' and 'branchesIgnore' defined") {
            val exception = shouldThrow<IllegalArgumentException> {
                Push(
                    branches = listOf("branch1"),
                    branchesIgnore = listOf("branch2"),
                )
            }
            exception.message shouldBe "Cannot define both 'branches' and 'branchesIgnore'!"
        }

        test("both 'tags' and 'tagsIgnore' defined") {
            val exception = shouldThrow<IllegalArgumentException> {
                Push(
                    tags = listOf("tag2"),
                    tagsIgnore = listOf("tag2"),
                )
            }
            exception.message shouldBe "Cannot define both 'tags' and 'tagsIgnore'!"
        }

        test("both 'paths' and 'pathsIgnore' defined") {
            val exception = shouldThrow<IllegalArgumentException> {
                Push(
                    paths = listOf("path1"),
                    pathsIgnore = listOf("path2"),
                )
            }
            exception.message shouldBe "Cannot define both 'paths' and 'pathsIgnore'!"
        }
    }
},)
