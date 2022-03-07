package it.krzeminski.githubactions.scriptgenerator

import it.krzeminski.githubactions.scriptmodel.GithubWorkflow
import it.krzeminski.githubactions.scriptmodel.myYaml
import kotlinx.serialization.decodeFromString
import java.net.URL

const val LIBRARY_VERSION = "0.9.0"

const val PACKAGE = "it.krzeminski.githubactions"

const val QUOTE = "\""

fun main(args: Array<String>) {
    if (args.isEmpty() || args.first().startsWith("http").not()) {
        error(
            """
            Usage:
              ./gradlew :script-generator:run --args https://raw.githubusercontent.com/jmfayard/refreshVersions/main/.github/workflows/publish-mkdocs-website.yml
            """.trimIndent()
        )
    }
    val url = URL(args.first())
    val urlContent = url.readText()
    val workflow: GithubWorkflow = myYaml.decodeFromString(urlContent)
    println(workflow.toKotlin(url.filename()))
}

fun URL.filename(): String =
    path.substringAfterLast("/").removeSuffix(".yml")
