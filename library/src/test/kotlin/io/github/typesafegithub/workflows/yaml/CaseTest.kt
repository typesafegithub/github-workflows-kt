package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest.Type
import io.github.typesafegithub.workflows.domain.triggers.PullRequestTarget
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class CaseTest : DescribeSpec({
    it("transforms to pascal case") {
        listOf(
            Type.Assigned to "assigned",
            Type.AutoMergeDisabled to "auto_merge_disabled",
            Type.ReviewRequested to "review_requested",
        ).forAll { (type, expected) ->
            type.toSnakeCase() shouldBe expected
        }
    }

    it("should fail early if the enum is not in pascal case") {
        MyEnum.values().forAll { enum ->
            shouldThrowAny {
                enum.toSnakeCase()
            }
        }
    }

    it("all enums should be in pascal case") {
        PullRequestTarget.Type.values().forAll { it.toSnakeCase() }
        PullRequest.Type.values().forAll { it.toSnakeCase() }
    }
})

private enum class MyEnum {
    In_valid, // ktlint-disable enum-entry-name-case
}
