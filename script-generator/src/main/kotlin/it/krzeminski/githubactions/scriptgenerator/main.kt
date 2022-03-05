package it.krzeminski.githubactions.scriptgenerator

import it.krzeminski.githubactions.scriptmodel.GithubWorkflow
import kotlinx.serialization.decodeFromString
import java.net.URL

fun main(args: Array<String>) {
    if (args.isEmpty() || args.first().startsWith("http").not()) {
        error(
            """
            Usage:
              ./gradlew :script-generator:run --args https://raw.githubusercontent.com/jmfayard/refreshVersions/main/.github/workflows/publish-mkdocs-website.yml
            """.trimIndent()
        )
    }
    val urlContent = URL(args.first()).readText()
    val workflow: GithubWorkflow = myYaml.decodeFromString(urlContent)
    println(workflow.toKotlin())
}
