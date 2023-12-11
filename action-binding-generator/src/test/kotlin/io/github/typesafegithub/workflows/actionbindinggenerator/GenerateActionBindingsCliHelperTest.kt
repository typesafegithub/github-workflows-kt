package io.github.typesafegithub.workflows.actionbindinggenerator

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe

class GenerateActionBindingsCliHelperTest : FunSpec({
    test("${::generateActionBindings.name} - smoke test") {
        // given
        val mockGitRootDir =
            tempdir().also {
                it.resolve(".git").mkdir()
                it.resolve(".github").resolve("workflows").also {
                    it.mkdirs()

                    it.resolve("_used-actions.yaml").writeText(
                        """
                        jobs:
                          some-job:
                            steps:
                              - uses: actions/checkout@v4
                              - uses: actions/setup-java@v4
                              - uses: gradle/gradle-build-action@v2
                        """.trimIndent(),
                    )

                    it.resolve("some-workflow.yaml").writeText(
                        """
                        jobs:
                          some-job:
                            steps:
                              - uses: actions/checkout@v4
                              - uses: gradle/gradle-build-action@v2
                        """.trimIndent(),
                    )
                }
            }

        // when
        generateActionBindings(arrayOf("some-workflow.yaml"), mockGitRootDir.toPath())

        // then
        val generatedDir = mockGitRootDir.resolve(".github").resolve("workflows").resolve("generated")
        val generatedFiles =
            generatedDir.walk().toList()
                .filter { it.isFile }
                .map { it.absolutePath.removePrefix(mockGitRootDir.absolutePath) }
                .sorted()
        generatedFiles shouldBe
            listOf(
                "/.github/workflows/generated/actions/checkout.kt",
                "/.github/workflows/generated/gradle/gradle-build-action.kt",
            )
    }
})
