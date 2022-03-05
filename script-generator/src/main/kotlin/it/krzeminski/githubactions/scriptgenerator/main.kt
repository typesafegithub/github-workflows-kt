package it.krzeminski.githubactions.scriptgenerator

import it.krzeminski.githubactions.scriptmodel.Workflow
import kotlinx.serialization.decodeFromString
import java.io.File

fun main() {
    val file = File("/Users/jmfayard/IdeaProjects/github-actions-kotlin-dsl/script-generator/src/test/resources/build.yml")
    val workflow: Workflow = myYaml.decodeFromString<Workflow>(file.readText())
    println(workflow)
}