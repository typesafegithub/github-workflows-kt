package it.krzeminski.githubactions.actions.endbug

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldInclude
import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.shouldHaveYamlArguments

class AddAndCommitV8Test : DescribeSpec({

    it("renders with default") {
        AddAndCommitV8() shouldHaveYamlArguments emptyMap()
    }

    it("renders with all parameters") {
        AddAndCommitV8(
            add = "src",
            commit = "commit",
            committerName = "Comitter Name",
            authorEmail = "mail@example.com",
            authorName = "Author Name",
            defaultAuthor = AddAndCommitV8.DefaultActor.GithubActor,
            cwd = "./path/to/the/repo",
            committerEmail = "mail@example.com",
            pathspecErrorHandling = AddAndCommitV8.PathSpecErrorHandling.Ignore,
            newBranch = "custom-new-branch",
            message = "Your commit message",
            tag = "v1.0.0 --force",
            remove = "./dir/old_file.js",
            push = false,
            pull = "--rebase --autostash ...",
        ) shouldHaveYamlArguments mapOf(
            "add" to "src",
            "author_name" to "Author Name",
            "author_email" to "mail@example.com",
            "commit" to "commit",
            "committer_name" to "Comitter Name",
            "committer_email" to "mail@example.com",
            "default_author" to "github_actor",
            "cwd" to "./path/to/the/repo",
            "pathspec_error_handling" to "ignore",
            "new_branch" to "custom-new-branch",
            "message" to "Your commit message",
            "remove" to "./dir/old_file.js",
            "push" to "false",
            "pull" to "--rebase --autostash ...",
            "tag" to "v1.0.0 --force",
        )
    }

    it("yamlOf() should check errors") {
        val testObject = object { }
        val action = object : Action("owner", "name", "V2") {
            override fun toYamlArguments() = yamlOf(
                "long" to 42L,
                "object" to testObject,
            )
        }

        shouldThrowAny {
            action.toYamlArguments()
        }.message shouldInclude "Invalid YAML properties: {long=42, object=$testObject}"
    }
})
