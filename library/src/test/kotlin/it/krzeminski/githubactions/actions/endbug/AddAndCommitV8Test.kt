package it.krzeminski.githubactions.actions.endbug

import io.kotest.core.spec.style.DescribeSpec
import it.krzeminski.githubactions.shouldHaveYamlArguments

class AddAndCommitV8Test : DescribeSpec({
    it("renders with default") {
        AddAndCommitV8() shouldHaveYamlArguments linkedMapOf()
    }

    it("renders with add") {
        AddAndCommitV8(
            add = "src"
        ) shouldHaveYamlArguments linkedMapOf("add" to "src")
    }

    it("renders with author_name") {
        AddAndCommitV8(
            authorName = "Author Name"
        ) shouldHaveYamlArguments linkedMapOf("author_name" to "Author Name")
    }

    it("renders with author_email") {
        AddAndCommitV8(
            authorEmail = "mail@example.com"
        ) shouldHaveYamlArguments linkedMapOf("author_email" to "mail@example.com")
    }

    it("renders with committer_name") {
        AddAndCommitV8(
            committerName = "Comitter Name"
        ) shouldHaveYamlArguments linkedMapOf("committer_name" to "Comitter Name")
    }

    it("renders with committer_email") {
        AddAndCommitV8(
            committerEmail = "mail@example.com"
        ) shouldHaveYamlArguments linkedMapOf("committer_email" to "mail@example.com")
    }

    it("renders with cwd") {
        AddAndCommitV8(
            cwd = "./path/to/the/repo"
        ) shouldHaveYamlArguments linkedMapOf("cwd" to "./path/to/the/repo")
    }

    it("renders with default_author") {
        AddAndCommitV8(
            defaultAuthor = "github_actor"
        ) shouldHaveYamlArguments linkedMapOf("default_author" to "github_actor")
    }

    it("renders with message") {
        AddAndCommitV8(
            message = "Your commit message"
        ) shouldHaveYamlArguments linkedMapOf("message" to "Your commit message")
    }

    it("renders with new_branch") {
        AddAndCommitV8(
            newBranch = "custom-new-branch"
        ) shouldHaveYamlArguments linkedMapOf("new_branch" to "custom-new-branch")
    }

    it("renders with pathspec_error_handling") {
        AddAndCommitV8(
            pathspecErrorHandling = "ignore"
        ) shouldHaveYamlArguments linkedMapOf("pathspec_error_handling" to "ignore")
    }

    it("renders with pull") {
        AddAndCommitV8(
            pull = "--rebase --autostash ..."
        ) shouldHaveYamlArguments linkedMapOf("pull" to "--rebase --autostash ...")
    }

    it("renders with push") {
        AddAndCommitV8(
            push = false
        ) shouldHaveYamlArguments linkedMapOf("push" to "false")
    }

    it("renders with remove") {
        AddAndCommitV8(
            remove = "./dir/old_file.js"
        ) shouldHaveYamlArguments linkedMapOf("remove" to "./dir/old_file.js")
    }

    it("renders with tag") {
        AddAndCommitV8(
            tag = "v1.0.0 --force"
        ) shouldHaveYamlArguments linkedMapOf("tag" to "v1.0.0 --force")
    }
})
