package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest.EventType
import io.github.typesafegithub.workflows.domain.triggers.PullRequestTarget
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class CaseTest :
    DescribeSpec({
        it("transforms to pascal case") {
            listOf(
                EventType.Assigned to "assigned",
                EventType.AutoMergeDisabled to "auto_merge_disabled",
                EventType.ReviewRequested to "review_requested",
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
            PullRequestTarget.EventType.values().forAll { it.toSnakeCase() }
            PullRequest.EventType.values().forAll { it.toSnakeCase() }
        }
    })

private enum class MyEnum {
    @Suppress("ktlint:standard:enum-entry-name-case")
    In_valid,
}
