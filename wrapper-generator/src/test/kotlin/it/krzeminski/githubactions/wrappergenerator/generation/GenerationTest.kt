package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfStringsTyping
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
                    required = true,
                    default = null,
                ),
                "baz-goo" to Input(
                    description = "Just another input",
                    required = true,
                    default = null,
                ),
            )
        )
        val coords = ActionCoords("john-smith", "simple-action-with-required-string-inputs", "v3")
        val fetchMetadataMock = mockk<ActionCoords.() -> Metadata>()
        every { fetchMetadataMock(any()) } returns actionManifest

        // when
        val wrapper = coords.generateWrapper(fetchMetadataImpl = fetchMetadataMock)
        writeToUnitTests(wrapper)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                // This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
                // be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
                // generator itself.
                package it.krzeminski.githubactions.actions.johnsmith

                import it.krzeminski.githubactions.actions.Action
                import kotlin.String
                import kotlin.Suppress

                /**
                 * Action: Do something cool
                 *
                 * This is a test description that should be put in the KDoc comment for a class
                 *
                 * [Action on GitHub](https://github.com/john-smith/simple-action-with-required-string-inputs)
                 */
                public class SimpleActionWithRequiredStringInputsV3(
                    /**
                     * Short description
                     */
                    public val fooBar: String,
                    /**
                     * Just another input
                     */
                    public val bazGoo: String
                ) : Action("john-smith", "simple-action-with-required-string-inputs", "v3") {
                    @Suppress("SpreadOperator")
                    public override fun toYamlArguments() = linkedMapOf(
                        *listOfNotNull(
                            "foo-bar" to fooBar,
                            "baz-goo" to bazGoo,
                        ).toTypedArray()
                    )
                }

            """.trimIndent(),
            filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/SimpleActionWithRequiredStringInputsV3.kt",
        )
    }

    test("action with various combinations of input parameters describing being required or optional") {
        // given
        val actionManifest = Metadata(
            name = "Do something cool",
            description = "This is a test description that should be put in the KDoc comment for a class",
            inputs = mapOf(
                "foo-bar" to Input(
                    description = "Required is default, default is set",
                    default = "barfoo",
                ),
                "baz-goo" to Input(
                    description = "Required is default, default is null",
                    default = null,
                ),
                "zoo-dar" to Input(
                    description = "Required is false, default is set",
                    required = false,
                    default = "googoo",
                ),
                "coo-poo" to Input(
                    description = "Required is false, default is default",
                    required = false,
                ),
                "bon-ton" to Input(
                    description = "Required is true, default is default",
                    required = true,
                ),
            )
        )
        val coords = ActionCoords("john-smith", "action-with-some-optional-inputs", "v3")
        val fetchMetadataMock = mockk<ActionCoords.() -> Metadata>()
        every { fetchMetadataMock(any()) } returns actionManifest

        // when
        val wrapper = coords.generateWrapper(fetchMetadataImpl = fetchMetadataMock)
        writeToUnitTests(wrapper)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                // This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
                // be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
                // generator itself.
                package it.krzeminski.githubactions.actions.johnsmith

                import it.krzeminski.githubactions.actions.Action
                import kotlin.String
                import kotlin.Suppress

                /**
                 * Action: Do something cool
                 *
                 * This is a test description that should be put in the KDoc comment for a class
                 *
                 * [Action on GitHub](https://github.com/john-smith/action-with-some-optional-inputs)
                 */
                public class ActionWithSomeOptionalInputsV3(
                    /**
                     * Required is default, default is set
                     */
                    public val fooBar: String? = null,
                    /**
                     * Required is default, default is null
                     */
                    public val bazGoo: String? = null,
                    /**
                     * Required is false, default is set
                     */
                    public val zooDar: String? = null,
                    /**
                     * Required is false, default is default
                     */
                    public val cooPoo: String? = null,
                    /**
                     * Required is true, default is default
                     */
                    public val bonTon: String
                ) : Action("john-smith", "action-with-some-optional-inputs", "v3") {
                    @Suppress("SpreadOperator")
                    public override fun toYamlArguments() = linkedMapOf(
                        *listOfNotNull(
                            fooBar?.let { "foo-bar" to it },
                            bazGoo?.let { "baz-goo" to it },
                            zooDar?.let { "zoo-dar" to it },
                            cooPoo?.let { "coo-poo" to it },
                            "bon-ton" to bonTon,
                        ).toTypedArray()
                    )
                }

            """.trimIndent(),
            filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/ActionWithSomeOptionalInputsV3.kt",
        )
    }

    test("action with non-string inputs") {
        // given
        val actionManifest = Metadata(
            name = "Do something cool",
            description = "This is a test description that should be put in the KDoc comment for a class",
            inputs = mapOf(
                "foo-bar" to Input(
                    description = "Short description",
                    required = true,
                    default = null,
                ),
                "baz-goo" to Input(
                    description = "First boolean input!",
                    required = true,
                    default = null,
                ),
                "bin-kin" to Input(
                    description = "Boolean and nullable",
                    required = false,
                    default = "test",
                ),
                "int-pint" to Input(
                    description = "Integer",
                    required = true,
                    default = null,
                ),
                "boo-zoo" to Input(
                    description = "List of strings",
                    required = true,
                    default = null,
                ),
                "fin-bin" to Input(
                    description = "Enumeration",
                    required = true,
                    default = null,
                ),
                "goo-zen" to Input(
                    description = "Integer with special value",
                    required = true,
                    default = null,
                )
            )
        )
        val coords = ActionCoords("john-smith", "action-with-non-string-inputs", "v3")
        val fetchMetadataMock = mockk<ActionCoords.() -> Metadata>()
        every { fetchMetadataMock(any()) } returns actionManifest

        // when
        val wrapper = coords.generateWrapper(
            fetchMetadataImpl = fetchMetadataMock,
            inputTypings = mapOf(
                "baz-goo" to BooleanTyping,
                "bin-kin" to BooleanTyping,
                "int-pint" to IntegerTyping,
                "boo-zoo" to ListOfStringsTyping(","),
                "fin-bin" to EnumTyping("Bin", listOf("foo", "boo-bar", "baz123")),
                "goo-zen" to IntegerWithSpecialValueTyping("Zen", mapOf("Special1" to 3, "Special2" to -1)),
            ),
        )
        writeToUnitTests(wrapper)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                // This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
                // be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
                // generator itself.
                package it.krzeminski.githubactions.actions.johnsmith

                import it.krzeminski.githubactions.actions.Action
                import kotlin.Boolean
                import kotlin.Int
                import kotlin.String
                import kotlin.Suppress
                import kotlin.collections.List

                /**
                 * Action: Do something cool
                 *
                 * This is a test description that should be put in the KDoc comment for a class
                 *
                 * [Action on GitHub](https://github.com/john-smith/action-with-non-string-inputs)
                 */
                public class ActionWithNonStringInputsV3(
                    /**
                     * Short description
                     */
                    public val fooBar: String,
                    /**
                     * First boolean input!
                     */
                    public val bazGoo: Boolean,
                    /**
                     * Boolean and nullable
                     */
                    public val binKin: Boolean? = null,
                    /**
                     * Integer
                     */
                    public val intPint: Int,
                    /**
                     * List of strings
                     */
                    public val booZoo: List<String>,
                    /**
                     * Enumeration
                     */
                    public val finBin: ActionWithNonStringInputsV3.Bin,
                    /**
                     * Integer with special value
                     */
                    public val gooZen: ActionWithNonStringInputsV3.Zen
                ) : Action("john-smith", "action-with-non-string-inputs", "v3") {
                    @Suppress("SpreadOperator")
                    public override fun toYamlArguments() = linkedMapOf(
                        *listOfNotNull(
                            "foo-bar" to fooBar,
                            "baz-goo" to bazGoo.toString(),
                            binKin?.let { "bin-kin" to it.toString() },
                            "int-pint" to intPint.toString(),
                            "boo-zoo" to booZoo.joinToString(","),
                            "fin-bin" to finBin.stringValue,
                            "goo-zen" to gooZen.integerValue.toString(),
                        ).toTypedArray()
                    )
    
                    public sealed class Bin(
                        public val stringValue: String
                    ) {
                        public object Foo : ActionWithNonStringInputsV3.Bin("foo")

                        public object BooBar : ActionWithNonStringInputsV3.Bin("boo-bar")

                        public object Baz123 : ActionWithNonStringInputsV3.Bin("baz123")

                        public class Custom(
                            customStringValue: String
                        ) : ActionWithNonStringInputsV3.Bin(customStringValue)
                    }

                    public sealed class Zen(
                        public val integerValue: Int
                    ) {
                        public class Value(
                            requestedValue: Int
                        ) : ActionWithNonStringInputsV3.Zen(requestedValue)

                        public object Special1 : ActionWithNonStringInputsV3.Zen(3)

                        public object Special2 : ActionWithNonStringInputsV3.Zen(-1)
                    }
                }

            """.trimIndent(),
            filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/ActionWithNonStringInputsV3.kt",
        )
    }


    test("Detect wrapper request with invalid properties") {
        // given
        val input = Input("input", "default", required = true)
        val actionManifest = Metadata(
            name = "Do something cool",
            description = "This is a test description that should be put in the KDoc comment for a class",
            inputs = mapOf(
                "foo-bar" to input,
                "baz-goo" to input
            )
        )
        val request = WrapperRequest(
            ActionCoords("actions", "setup-node", "v2"),
            inputTypings = mapOf(
                "check-latest" to BooleanTyping,
                "foo-bar" to BooleanTyping,
                "bazGoo" to BooleanTyping,
            )
        )

        shouldThrowAny {
            // when
            checkPropertiesAreValid(actionManifest, request.inputTypings)
        }.shouldHaveMessage(
            // then
            """
            Request contains invalid properties:
            Available: [foo-bar, baz-goo]
            Invalid:   [check-latest, bazGoo]
        """.trimIndent())
    }
})
