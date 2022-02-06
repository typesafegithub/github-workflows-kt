import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GenerationTest : FunSpec({
    test("action with required inputs as strings, no outputs") {
        // given
        val actionManifest = Manifest(
            name = "Do something cool",
            description = "This is a test description that should be put in the KDoc comment for a class",
            author = "John Smith",
            inputs = mapOf(
                "foo-bar" to Input(
                    description = "Short description",
                    required = true,
                ),
                "baz-goo" to Input(
                    description = "Just another input",
                    required = true,
                ),
            )
        )
        val coords = ActionCoords("john-smith", "do-sth-cool", "v3")
        val fetchManifestMock = mockk<ActionCoords.() -> Manifest>()
        every { fetchManifestMock(any()) } returns actionManifest

        // when
        val wrapper = coords.generateWrapper(fetchManifestMock)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                package it.krzeminski.githubactions.actions.actions

                import it.krzeminski.githubactions.actions.Action

                class DoSthCoolV3(
                    val fooBar: String,
                    val bazGoo: String,
                ) : Action("john-smith", "do-sth-cool", "v3") {
                    override fun toYamlArguments() = linkedMapOf(
                        "foo-bar" to fooBar,
                        "baz-goo" to bazGoo,
                    )
                }
            """.trimIndent(),
            filePath = "library/src/main/kotlin/it/krzeminski/githubactions/actions/johnsmith/DoSthCoolV3.kt",
        )
    }
})
