package io.github.typesafegithub.workflows.codegenerator

import io.github.typesafegithub.workflows.dsl.expressions.generateEventPayloads
import java.nio.file.Paths

/***
 * Either run this main() function or run this command: ./gradlew :code-generator:run
 */
fun main() {
    // To ensure there are no leftovers from previous generations.
    Paths.get("github-workflows-kt/src/gen").toFile().deleteRecursively()
    generateEventPayloads()
}
