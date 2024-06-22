package io.github.typesafegithub.workflows

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.model.GradleProject
import java.io.File
import java.util.Locale

fun main() {
    GradleConnector.newConnector()
        .forProjectDirectory(File("/Users/piotr/repos/github-workflows-kt"))
        .connect()
        .use { gradleConnector ->
            val model = gradleConnector.getModel(GradleProject::class.java)
            val allTasks = model.tasks + model.children.all.flatMap { it.tasks }
            allTasks.forEach { task ->
                println(task)
            }
            println("Generate the following accessors:")
            allTasks.forEach { task ->
                println("projects" + task.path.split(":").joinToString(".") { it.toCamelCase() })
            }
        }
}

internal fun String.toPascalCase(): String {
    val hasOnlyUppercases = none { it in 'a'..'z' }
    val normalizedString = if (hasOnlyUppercases) lowercase() else this
    return normalizedString.replace("+", "-plus-")
        .split("-", "_", " ", ".", "/")
        .joinToString("") {
            it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
}

internal fun String.toCamelCase(): String = toPascalCase().replaceFirstChar { it.lowercase() }
