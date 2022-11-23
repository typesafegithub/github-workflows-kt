package test

import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.scriptgenerator.decodeYamlWorkflow
import it.krzeminski.githubactions.scriptgenerator.rootProject
import it.krzeminski.githubactions.scriptgenerator.toFileSpec
import it.krzeminski.githubactions.scriptmodel.YamlWorkflow
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import java.io.FileFilter

class GenerateKotlinScripts : FunSpec({

    val testInputs = rootProject.resolve("script-generator/yaml-input")
        .listFiles(FileFilter { it.extension == "yaml" })!!
        .map { it.name }

    testInputs.forEach { name ->
        test("Generating $name") {
            val input = TestInput(name)
            val workflow: YamlWorkflow = decodeYamlWorkflow(input.yamlFile.readText())

            val newContent = workflow.toFileSpec(workflow.name)
                .toBuilder(packageName = "generated").build()
                .toString()
                .removeWindowsEndings()

            input.actualFile.writeText(
                "package actual\n" + newContent.removePrefix("package generated"),
            )
            newContent shouldBe input.expected

            if (System.getenv("GITHUB_ACTIONS") == "true") {
                newContent shouldBe input.expected
            } else if (newContent == input.expected) {
                input.actualFile.delete()
            } else {
                fail("${input.expectedFile.name} != ${input.actualFile.name} in ${input.actualFile.parentFile.canonicalPath}")
            }
        }
    }
},)

data class TestInput(val name: String) {
    val filename = name.removeSuffix(".yml")
    val yamlFile = rootProject.resolve("script-generator/yaml-input/$filename.yml")
        .also { require(it.canRead()) { "Invalid file ${it.canonicalPath}" } }

    private fun file(path: String) = rootProject.resolve("script-generator/src/test/kotlin/generated/$path")
    val expectedFile = file("${filename.toPascalCase()}.kt")
    val actualDir = rootProject.resolve("script-generator/src/test/kotlin/actual")
        .also { it.mkdirs() }
    val actualFile = actualDir.resolve("${filename.toPascalCase()}.kt")
    val expected = if (expectedFile.canRead()) {
        expectedFile.readText()
            .removeWindowsEndings()
    } else {
        ""
    }
}

fun String.removeWindowsEndings(): String =
    replace("\r\n", "\n")
