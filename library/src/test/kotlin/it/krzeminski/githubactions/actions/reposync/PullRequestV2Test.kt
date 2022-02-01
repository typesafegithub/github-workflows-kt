package it.krzeminski.githubactions.actions.reposync

import io.kotest.core.spec.style.DescribeSpec
import it.krzeminski.githubactions.shouldHaveYamlArguments

class PullRequestV2Test : DescribeSpec({

    it("renders with defaults") {
        PullRequestV2() shouldHaveYamlArguments linkedMapOf()
    }

    it("renders with source_branch") {
        PullRequestV2(
            sourceBranch = "main"
        ) shouldHaveYamlArguments linkedMapOf("source_branch" to "main")
    }

    it("renders with destination_branch") {
        PullRequestV2(
            destinationBranch = "dest"
        ) shouldHaveYamlArguments linkedMapOf("destination_branch" to "dest")
    }

    it("renders with pr_title") {
        PullRequestV2(
            prTitle = "Pull Request title"
        ) shouldHaveYamlArguments linkedMapOf("pr_title" to "Pull Request title")
    }

    it("renders with pr_body") {
        PullRequestV2(
            prBody = "PR Body"
        ) shouldHaveYamlArguments linkedMapOf("pr_body" to "PR Body")
    }

    it("renders with pr_template") {
        PullRequestV2(
            prTemplate = "my_template"
        ) shouldHaveYamlArguments linkedMapOf("pr_template" to "my_template")
    }

    it("renders with pr_reviewer") {
        PullRequestV2(
            prReviewer = "someone,someone_else"
        ) shouldHaveYamlArguments linkedMapOf("pr_reviewer" to "someone,someone_else")
    }

    it("renders with pr_assignee") {
        PullRequestV2(
            prAssignee = "assignee"
        ) shouldHaveYamlArguments linkedMapOf("pr_assignee" to "assignee")
    }

    it("renders with pr_label") {
        PullRequestV2(
            prLabel = "PR label"
        ) shouldHaveYamlArguments linkedMapOf("pr_label" to "PR label")
    }

    it("renders with pr_milestone") {
        PullRequestV2(
            prMilestone = "PR milestone"
        ) shouldHaveYamlArguments linkedMapOf("pr_milestone" to "PR milestone")
    }

    it("renders with pr_draft") {
        PullRequestV2(
            prDraft = false
        ) shouldHaveYamlArguments linkedMapOf("pr_draft" to "false")
    }

    it("renders with pr_allow_empty") {
        PullRequestV2(
            prAllowEmpty = false
        ) shouldHaveYamlArguments linkedMapOf("pr_allow_empty" to "false")
    }

    it("renders with github_token") {
        PullRequestV2(
            githubToken = "GitHub token"
        ) shouldHaveYamlArguments linkedMapOf("github_token" to "GitHub token")
    }
})
