package it.krzeminski.githubactions.scriptgenerator

import it.krzeminski.githubactions.scriptmodel.YamlWorkflow
import it.krzeminski.githubactions.scriptmodel.myYaml
import it.krzeminski.githubactions.scriptmodel.normalizeYaml
import kotlinx.serialization.decodeFromString
import java.io.File
import java.net.URL

val LIBRARY_VERSION by lazy {
    rootProject.resolve("version.txt").readText().trim()
}

val rootProject = File(".").canonicalFile.let {
    if (it.name == "github-actions-kotlin-dsl") it else it.parentFile
}

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
    val workflow: YamlWorkflow = decodeYamlWorkflow(urlContent)
    println(workflow.toKotlin(url.filename()))
}

fun decodeYamlWorkflow(text: String) : YamlWorkflow {
    return myYaml.decodeFromString(text.normalizeYaml())
}



fun URL.filename(): String =
    path.substringAfterLast("/").removeSuffix(".yml")
