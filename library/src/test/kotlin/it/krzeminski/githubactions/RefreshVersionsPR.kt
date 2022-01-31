package it.krzeminski.githubactions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.Steps.userCheckout
import it.krzeminski.githubactions.Steps.usesCommitChanges
import it.krzeminski.githubactions.Steps.usesCreatePullRequest
import it.krzeminski.githubactions.Steps.usesSetupJava11
import it.krzeminski.githubactions.actions.actions.AddAndCommitV8
import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.CreateBranchV210
import it.krzeminski.githubactions.actions.actions.PullRequestV2
import it.krzeminski.githubactions.actions.actions.SetupJavaV2
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.JobBuilder
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.file.Paths

class RefreshVersionsPR : FunSpec({

    test("Check workflow") {
        val everyMondayAt7am = Cron(minute = "0", hour = "7", dayWeek = "1")

        val workflow = workflow(
            name = "RefreshVersions PR",
            on = listOf(
                Push(),
                PullRequest,
                WorkflowDispatch,
                Schedule(listOf(everyMondayAt7am))
            ),
            sourceFile = Paths.get("RefreshVersionsBot.kts"),
            targetFile = Paths.get("refreshversions_bot.yaml"),
        ) {
            job("Refresh-Versions", UbuntuLatest) {
                userCheckout(branch = "main")
                usesSetupJava11()
                uses("create-branch", CreateBranchV210(branch = Branches.DEPENDENCY_UPDATE))
                uses("refreshVersions", GradleBuildActionV2(arguments = "refreshVersions"))
                usesCommitChanges(Branches.DEPENDENCY_UPDATE, "Refresh versions.properties")
                usesCreatePullRequest()
            }
        }

        workflow.toYaml(addConsistencyCheck = false).trim() shouldBe expectedYaml.trim()
        println(workflow.toYaml(addConsistencyCheck = false))
    }
})

object Branches {
    const val MAIN = "main"
    const val DEPENDENCY_UPDATE = "dependency-update"
}

object Steps {
    fun JobBuilder.userCheckout(branch: String) = uses(
        name = "Check out",
        action = CheckoutV2(ref = branch),
    )

    fun JobBuilder.usesSetupJava11() = uses(
        name = "setup-java",
        action = SetupJavaV2(
            distribution = SetupJavaV2.Distribution.Adopt,
            javaVersion = "11"
        )
    )

    fun JobBuilder.usesCommitChanges(branch: String, message: String) = uses(
        name = "Commit",
        AddAndCommitV8(
            author_name = "GitHub Actions",
            author_email = "noreply@github.com",
            new_branch = branch,
            message = message
        )
    )

    fun JobBuilder.usesCreatePullRequest() = uses(
        name = "Pull Request",
        action = PullRequestV2(
            source_branch = Branches.DEPENDENCY_UPDATE,
            destination_branch = Branches.MAIN,
            pr_draft = true,
            pr_title = "Upgrade gradle dependencies",
            github_token = "\${{ secrets.GITHUB_TOKEN }}"
        )
    )
}

val expectedYaml = """
# This file was generated using Kotlin DSL (RefreshVersionsBot.kts).
# If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
# Generated with https://github.com/krzema12/github-actions-kotlin-dsl

name: RefreshVersions PR

on:
  push:
  pull_request:
  workflow_dispatch:
  schedule:
   - cron: '0 7 * * 1'

jobs:
  "Refresh-Versions":
    runs-on: "ubuntu-latest"
    steps:
      - name: Check out
        uses: actions/checkout@v2
        with:
          ref: ${Branches.MAIN}
      - name: setup-java
        uses: actions/setup-java@v2
        with:
          distribution: adopt
          java-version: 11
      - name: create-branch
        uses: peterjgrainger/action-create-branch@v2.0.1
        with:
          branch: ${Branches.DEPENDENCY_UPDATE}
      - name: refreshVersions
        uses: gradle/gradle-build-action@v2
        with:
          arguments: refreshVersions
      - name: Commit
        uses: EndBug/add-and-commit@v8
        with:
          author_name: GitHub Actions
          author_email: noreply@github.com
          message: Refresh versions.properties
          new_branch: dependency-update
      - name: Pull Request
        uses: repo-sync/pull-request@v2.6.1
        with:
          source_branch: dependency-update
          destination_branch: main
          pr_title: Upgrade gradle dependencies
          pr_draft: true
          github_token: ${'$'}{{ secrets.GITHUB_TOKEN }}          
""".trimIndent()
