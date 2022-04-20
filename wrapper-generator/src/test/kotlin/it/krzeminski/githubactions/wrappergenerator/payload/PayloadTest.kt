package it.krzeminski.githubactions.wrappergenerator.payload

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PayloadTest : FunSpec({
    test("Type-safe expressions for pull-request event") {
        event.action shouldBe "event.action"
        event.sender.login shouldBe "event.sender.login"
        event.pullRequest.commits shouldBe "event.pull_request.commits"
    }

    test("Type-safe expressions for GitHub's payload") {
        github.token shouldBe "github.token"
        github.env shouldBe "github.env"
        github.job shouldBe "github.job"
    }

    test("Arrays") {
        event.pullRequest.assignees[1] shouldBe "event.pull_request.assignees[1]"
        event.pullRequest.labels[1] shouldBe "event.pull_request.labels[1]"
    }
})
