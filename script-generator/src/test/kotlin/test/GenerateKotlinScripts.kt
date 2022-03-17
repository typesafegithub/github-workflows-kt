package test

import generated.allWorkflows
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.scriptgenerator.decodeYamlWorkflow
import it.krzeminski.githubactions.scriptgenerator.filename
import it.krzeminski.githubactions.scriptgenerator.rootProject
import it.krzeminski.githubactions.scriptgenerator.toFileSpec
import it.krzeminski.githubactions.scriptmodel.YamlWorkflow
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import it.krzeminski.githubactions.yaml.writeToFile
import java.io.FileFilter
import java.net.URL

class GenerateKotlinScripts : FunSpec({

    val testInputs = rootProject.resolve("script-generator/yaml-input")
        .listFiles(FileFilter { it.extension == "yml" })!!
        .map { it.name }

    testInputs.forEach { name ->
        test("Generating $name") {
            val input = TestInput(name)
            val workflow: YamlWorkflow = decodeYamlWorkflow(input.yamlFile.readText())

            val newContent = workflow.toFileSpec(workflow.name, "yaml-output")
                .toBuilder(packageName = "generated").build()
                .toString()
                .removeWindowsEndings()

            input.actualFile.writeText(
                "package actual\n" + newContent.removePrefix("package generated")
            )

            if (System.getenv("GITHUB_ACTIONS") == "true") {
                newContent shouldBe input.expected
            } else if (newContent == input.expected) {
                input.actualFile.delete()
            } else {
                fail("${input.expectedFile.name} != ${input.actualFile.name} in ${input.actualFile.parentFile.canonicalPath}")
            }
        }
    }

    test("filename from URL") {
        val url =
            URL("https://raw.githubusercontent.com/jmfayard/refreshVersions/main/.github/workflows/publish-mkdocs-website.yml")
        url.filename() shouldBe "publish-mkdocs-website"
    }

    test("Execute Kotlin Scripts") {
        allWorkflows.forEach { it.writeToFile(addConsistencyCheck = false) }
    }
})

data class TestInput(val name: String) {
    val filename = name.removeSuffix(".yml")
    val yamlFile = rootProject.resolve("script-generator/yaml-input/$filename.yml")
        .also { require(it.canRead()) { "Invalid file ${it.canonicalPath}" } }
    private fun file(path: String) = rootProject.resolve("script-generator/src/test/kotlin/generated/$path")
    val expectedFile = file("${filename.toPascalCase()}.kt")
    val actualFile = file("../actual/${filename.toPascalCase()}.kt")
    val expected = if (expectedFile.canRead())
        expectedFile.readText()
            .removeWindowsEndings()
    else ""
}

fun String.removeWindowsEndings(): String =
    replace("\r\n", "\n")
