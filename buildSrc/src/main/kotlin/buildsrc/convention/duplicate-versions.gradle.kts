package buildsrc.convention

import org.gradle.api.Task
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.getValue

val validateDuplicatedVersion by tasks.registering(Task::class) {
    // These properties of a project need to be resolved before the 'doLast' block because otherwise, Gradle
    // configuration cache cannot be used.
    val rootDir = rootDir
    val version = version

    doLast {
        require(
            rootDir.resolve("automation/code-generator/src/main/kotlin/io/github/typesafegithub/workflows/codegenerator/ActionsDocsGeneration.kt").readText()
                .contains("private const val LIB_VERSION = \"$version\"")
        ) { "Library version stated in the docs should be equal to $version!" }
        require(
            rootDir.resolve("github-workflows-kt/src/test/kotlin/io/github/typesafegithub/workflows/docsnippets/GettingStartedSnippets.kt").readText()
                .contains("\"io.github.typesafegithub:github-workflows-kt:$version\"")
        ) { "Library version stated in github-workflows-kt/src/test/.../GettingStarted.kt should be equal to $version!" }
        require(
            rootDir.resolve(".github/workflows/end-to-end-tests.main.kts").readText()
                .contains("\"io.github.typesafegithub:github-workflows-kt:$version\"")
        ) { "Library version stated in end-to-end-tests.main.kts should be equal to $version!" }
        require(
            rootDir.resolve(".github/workflows/end-to-end-tests.main.kts").readText()
                .contains("\"io.github.typesafegithub:action-updates-checker:$version\"")
        ) { "Library version stated in end-to-end-tests.main.kts should be equal to $version!" }
        require(
            rootDir.resolve("images/teaser-with-newest-version.svg").readText()
                .contains("\"io.github.typesafegithub:github-workflows-kt:$version\"")
        ) { "Library version stated in the teaser image shiuld be equal to $version!" }
    }
}

project.tasks.getByName("check") {
    dependsOn(validateDuplicatedVersion)
}
