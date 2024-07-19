package io.github.typesafegithub.workflows.gradle

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.model.GradleProject
import java.io.File

internal fun main() {
    val taskTreeJson = createTaskTreeJson(gitRepoRoot = File("/Users/piotr/repos/github-workflows-kt"))
    println(taskTreeJson)
    // It will be stored somewhere.

    val taskTree = loadTaskTreeJson(taskTreeJson)
    println(taskTree)

    @Suppress("UNCHECKED_CAST")
    val automation = taskTree["automation"] as Map<String, Any>

    @Suppress("UNCHECKED_CAST")
    val codeGenerator = automation["code-generator"] as Map<String, Any>
    val build = codeGenerator["build"]
    println(build) // Expected: "task", which means a leaf in the tree.
}

internal fun createTaskTreeJson(gitRepoRoot: File): String =
    GradleConnector
        .newConnector()
        .forProjectDirectory(gitRepoRoot)
        .connect()
        .use { gradleConnector ->
            val model = gradleConnector.getModel(GradleProject::class.java)
            model.readTasksRecursively()
        }.let { json.encodeToString(it) }

internal fun loadTaskTreeJson(taskTreeJson: String): Map<String, Any> = json.decodeFromString<JsonObject>(taskTreeJson)

private fun GradleProject.readTasksRecursively(): JsonObject =
    JsonObject(
        content =
            this.tasks.associate { it.name to JsonPrimitive("task") } +
                this.children.associate { it.name to it.readTasksRecursively() },
    )

private val json = Json { prettyPrint = true }
