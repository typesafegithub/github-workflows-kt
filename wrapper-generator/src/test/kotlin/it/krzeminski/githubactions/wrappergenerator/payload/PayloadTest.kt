package it.krzeminski.githubactions.wrappergenerator.payload

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PayloadTest : FunSpec({
    test("Type-safe expressions for pull-request event") {
        """
        action = ${event.action}
        sender = ${event.sender.login}
        commits = ${event.pullRequest.commits}
        """.trimIndent() shouldBe """
        action = event.action
        sender = event.sender.login
        commits = event.pull_request.commits
        """.trimIndent()
    }

    test("Type-safe expressions for GitHub's payload") {
        """
        token = ${github.token}
        env = ${github.env}
        job = ${github.job}
        """.trimIndent() shouldBe """
        token = github.token
        env = github.env
        job = github.job
        """.trimIndent()
    }
})
