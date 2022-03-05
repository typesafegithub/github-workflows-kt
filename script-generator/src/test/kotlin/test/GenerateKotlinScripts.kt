package test

import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import it.krzeminski.githubactions.scriptgenerator.myYaml
import it.krzeminski.githubactions.scriptgenerator.toFileSpec
import it.krzeminski.githubactions.scriptmodel.GithubWorkflow
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import kotlinx.serialization.decodeFromString
import java.io.File

class GenerateKotlinScripts : FunSpec({

    val testInputs = listOf(
        "refreshversions-pr.yml",
        "generate-wrappers.yml",
        "generated-source.yml",
        "update-gradle-wrapper.yml",
        "refreshversions-build.yml",
        "refreshversions-website.yml",
    )

    testInputs.forEach { name ->
        test("Generating $name") {
            val input = TestInput(name)
            val workflow: GithubWorkflow = myYaml.decodeFromString(input.yamlFile.readText())
            input.initialFile.writeText(input.expected.replace("package generated", "package initial"))
            val newContent = workflow.toFileSpec().toString().run {
                "package generated\n" + removeRange(0, indexOf("import"))
            }
            input.kotlinFile.writeText(newContent)
            if (input.kotlinFile.readText() == input.expected) {
                input.initialFile.delete()
            } else {
                fail("${input.kotlinFile} and ${input.initialFile} differ")
            }
        }
    }
})

data class TestInput(val name: String) {
    val filename = name.removeSuffix(".yml")
    val yamlFile = File("src/test/resources/$filename.yml")
        .also { require(it.canRead()) { "Invalid file ${it.canonicalPath}" } }
    val kotlinFile = File("src/test/kotlin/generated/${filename.toPascalCase()}.kt")
    val initialFile = File("src/test/kotlin/generated/${filename.toPascalCase()}Initial.kt")
    val expected = if (kotlinFile.canRead()) kotlinFile.readText() else ""
}