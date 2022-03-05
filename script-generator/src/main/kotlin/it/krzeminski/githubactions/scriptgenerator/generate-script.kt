package it.krzeminski.githubactions.scriptgenerator

import it.krzeminski.githubactions.scriptmodel.Workflow
import kotlinx.serialization.decodeFromString
import java.io.File

fun main() {
    val file = File(".github/workflows/generated-source.yml")
    val destinationFile = File(".github/workflows/generated.main.kts")
    val workflow: Workflow = myYaml.decodeFromString(file.readText())
    println("\nWritten to ${destinationFile.canonicalPath}")
    destinationFile.writeText(workflow.toKotlin())
}