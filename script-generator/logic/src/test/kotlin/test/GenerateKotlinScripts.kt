package test

import io.github.typesafegithub.workflows.scriptgenerator.decodeYamlWorkflow
import io.github.typesafegithub.workflows.scriptgenerator.rootProject
import io.github.typesafegithub.workflows.scriptgenerator.toFileSpec
import io.github.typesafegithub.workflows.scriptmodel.YamlWorkflow
import io.github.typesafegithub.workflows.wrappergenerator.generation.toPascalCase
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.FileFilter

class GenerateKotlinScripts : FunSpec({

    val testInputs = rootProject.resolve("script-generator/logic/yaml-input")
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
})

data class TestInput(val name: String) {
    val filename = name.removeSuffix(".yml")
    val yamlFile = rootProject.resolve("script-generator/logic/yaml-input/$filename.yml")
        .also { require(it.canRead()) { "Invalid file ${it.canonicalPath}" } }

    private fun file(path: String) = rootProject.resolve("script-generator/logic/src/test/kotlin/generated/$path")
    val expectedFile = file("${filename.toPascalCase()}.kt")
    val actualDir = rootProject.resolve("script-generator/logic/src/test/kotlin/actual")
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
