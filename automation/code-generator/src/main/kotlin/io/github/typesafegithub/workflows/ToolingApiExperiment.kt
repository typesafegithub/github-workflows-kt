package io.github.typesafegithub.workflows

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.model.GradleProject
import java.io.File

fun main() {
    GradleConnector.newConnector()
        .forProjectDirectory(File("/Users/piotr/repos/github-workflows-kt"))
        .connect()
        .use { gradleConnector ->
            val model = gradleConnector.getModel(GradleProject::class.java)
            val projects = model.children.all
            projects.forEach { project ->
                project.tasks.forEach { task ->
                    println(task)
                }
            }
        }
}
