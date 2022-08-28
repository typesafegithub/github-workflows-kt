#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.25.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3.Distribution.Zulu
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.writeToFile

workflow(
    name = "Check if wrappers up to date",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(
            // A workaround for https://github.com/krzema12/github-actions-kotlin-dsl/issues/416
            // Otherwise, a YAML anchor/alias is used here and GitHub cannot parse the YAML.
            branchesIgnore = listOf("some-non-existent-branch"),
        ),
        Schedule(triggers = listOf(Cron(hour = "1", minute = "0"))),
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "check",
        runsOn = UbuntuLatest,
    ) {
        uses(CheckoutV3())
        uses(
            name = "Set up JDK",
            action = SetupJavaV3(
                javaVersion = "17",
                distribution = Zulu,
            )
        )
        uses(
            name = "Generate wrappers",
            action = GradleBuildActionV2(
                arguments = ":wrapper-generator:run",
            )
        )
        run(
            name = "Fail if there are any changes in the generated wrappers or their list in the docs",
            command = "git diff --exit-code library/src/gen/ docs/supported-actions.md"
        )
    }
}.writeToFile()
