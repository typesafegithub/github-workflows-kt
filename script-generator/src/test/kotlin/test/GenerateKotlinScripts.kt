package test

import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.scriptgenerator.decodeYamlWorkflow
import it.krzeminski.githubactions.scriptgenerator.filename
import it.krzeminski.githubactions.scriptgenerator.toFileSpec
import it.krzeminski.githubactions.scriptmodel.YamlWorkflow
import it.krzeminski.githubactions.scriptmodel.myYaml
import it.krzeminski.githubactions.scriptmodel.normalizeYaml
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.FileFilter
import java.net.URL

class GenerateKotlinScripts : FunSpec({

    val testInputs = File("src/test/resources")
        .listFiles(FileFilter { it.extension == "yml" })!!
        .map { it.name }

    testInputs.forEach { name ->
        test("Generating $name") {
            val input = TestInput(name)
            val workflow: YamlWorkflow = decodeYamlWorkflow(input.yamlFile.readText())

            val newContent = workflow.toFileSpec(workflow.name)
                .toString()
                .removeWindowsEndings()

            input.actualFile.writeText("package actual\n\n$newContent")

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
        val url = URL("https://raw.githubusercontent.com/jmfayard/refreshVersions/main/.github/workflows/publish-mkdocs-website.yml")
        url.filename() shouldBe "publish-mkdocs-website"
    }
})

data class TestInput(val name: String) {
    val filename = name.removeSuffix(".yml")
    private fun file(path: String) = File("src/test/resources/$path")
    val yamlFile = file("$filename.yml")
        .also { require(it.canRead()) { "Invalid file ${it.canonicalPath}" } }
    val expectedFile = file("${filename.toPascalCase()}.kt")
    val actualFile = file("${filename.toPascalCase()}Actual.kt")
    val expected = if (expectedFile.canRead())
        expectedFile.readText()
            .removeWindowsEndings()
            .replace("package expected\n\n", "")
    else ""
}

fun String.removeWindowsEndings(): String =
    replace("\r\n", "\n")
