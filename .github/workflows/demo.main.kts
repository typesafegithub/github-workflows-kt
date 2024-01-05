#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.9.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.io.File
import java.time.LocalDate
import java.time.Period

workflow(
    name = "My workflow",
    sourceFile = __FILE__.toPath(),
    on = listOf(PullRequest()),
) {
    job(id = "my-job", runsOn = RunnerType.UbuntuLatest) {
        uses(action = CheckoutV4())
        // Before
        run(
            name = "Print dates in the past (shell script as string)",
            command = """
            today=$(date +%s)
   
            while IFS= read -r date; do
                date_seconds=$(date -d "${'$'}date" +%s)
                if [ ${'$'}date_seconds -lt ${'$'}today ]; then
                    diff=$(( (today - date_seconds) / (60 * 60 * 24) ))
                    echo "${'$'}date is in the past (${'$'}diff days ago)"
                fi
            done < "dates.txt"
            """
        )

        // After 🎉
        @OptIn(ExperimentalKotlinLogicStep::class)
        run(name = "Print dates in the past (inlined Kotlin)") {
            val today = LocalDate.now()

            File("dates.txt").readLines()
                .map { LocalDate.parse(it) }
                .filter { it < today }
                .forEach {
                    val diff = Period.between(it, today).days
                    println("$it is in the past ($diff days ago)")
                }
        }
    }
}.writeToFile()
