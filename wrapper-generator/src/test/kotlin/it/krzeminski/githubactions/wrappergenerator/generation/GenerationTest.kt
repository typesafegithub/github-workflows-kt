package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.metadata.Input
import it.krzeminski.githubactions.wrappergenerator.metadata.Metadata

class GenerationTest : FunSpec({
    test("action with required inputs as strings, no outputs") {
        // given
        val actionManifest = Metadata(
            name = "Do something cool",
            description = "This is a test description that should be put in the KDoc comment for a class",
            inputs = mapOf(
                "foo-bar" to Input(
                    description = "Short description",
                    default = null,
                ),
                "baz-goo" to Input(
                    description = "Just another input",
                    default = null,
                ),
            )
        )
        val coords = ActionCoords("john-smith", "do-sth-cool", "v3")
        val fetchMetadataMock = mockk<ActionCoords.() -> Metadata>()
        every { fetchMetadataMock(any()) } returns actionManifest

        // when
        val wrapper = coords.generateWrapper(fetchMetadataMock)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                // This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
                // be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
                // generator itself.
                package it.krzeminski.githubactions.actions.johnsmith

                import it.krzeminski.githubactions.actions.Action
                import kotlin.String

                /**
                 * Action: Do something cool
                 *
                 * This is a test description that should be put in the KDoc comment for a class
                 *
                 * http://github.com/john-smith/do-sth-cool
                 */
                public class DoSthCoolV3(
                    /**
                     * Short description
                     */
                    public val fooBar: String,
                    /**
                     * Just another input
                     */
                    public val bazGoo: String
                ) : Action("john-smith", "do-sth-cool", "v3") {
                    public override fun toYamlArguments() = linkedMapOf(
                        "foo-bar" to fooBar,
                        "baz-goo" to bazGoo,
                    )
                }

            """.trimIndent(),
            filePath = "library/src/main/kotlin/it/krzeminski/githubactions/actions/johnsmith/DoSthCoolV3.kt",
        )
    }
})
