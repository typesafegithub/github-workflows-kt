package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.metadata.Input
import it.krzeminski.githubactions.wrappergenerator.metadata.Metadata
import it.krzeminski.githubactions.wrappergenerator.metadata.Output

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
                    deprecationMessage = "this is deprecated",
                    required = true,
                    default = null,
                ),
            )
        )
        val coords = ActionCoords("john-smith", "simple-action-with-required-string-inputs", "v3")

        // when
        val wrapper = coords.generateWrapper { actionManifest }
        writeToUnitTests(wrapper)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                // This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
                // be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
                // generator itself.
                @file:Suppress("DEPRECATION")
                
                package it.krzeminski.githubactions.actions.johnsmith

                import it.krzeminski.githubactions.actions.Action
                import kotlin.Deprecated
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
                    @Deprecated("this is deprecated")
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
                "package" to Input(
                    description = "Required is true, default is default",
                    required = true,
                ),
            )
        )
        val coords = ActionCoords("john-smith", "action-with-some-optional-inputs", "v3")

        // when
        val wrapper = coords.generateWrapper(fetchMetadataImpl = { actionManifest })
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
                    public val `package`: String
                ) : Action("john-smith", "action-with-some-optional-inputs", "v3") {
                    @Suppress("SpreadOperator")
                    public override fun toYamlArguments() = linkedMapOf(
                        *listOfNotNull(
                            fooBar?.let { "foo-bar" to it },
                            bazGoo?.let { "baz-goo" to it },
                            zooDar?.let { "zoo-dar" to it },
                            cooPoo?.let { "coo-poo" to it },
                            "package" to `package`,
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
                ),
                "bah-enum" to Input(
                    description = "Enum with custom naming",
                    required = true,
                    default = null,
                ),
            )
        )
        val coords = ActionCoords("john-smith", "action-with-non-string-inputs", "v3")

        // when
        val wrapper = coords.generateWrapper(
            fetchMetadataImpl = { actionManifest },
            inputTypings = mapOf(
                "baz-goo" to BooleanTyping,
                "bin-kin" to BooleanTyping,
                "int-pint" to IntegerTyping,
                "boo-zoo" to ListOfTypings(","),
                "fin-bin" to EnumTyping("Bin", listOf("foo", "boo-bar", "baz123")),
                "goo-zen" to IntegerWithSpecialValueTyping("Zen", mapOf("Special1" to 3, "Special2" to -1)),
                "bah-enum" to EnumTyping("Bah", listOf("helloworld"), listOf("HelloWorld")),
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
                    public val gooZen: ActionWithNonStringInputsV3.Zen,
                    /**
                     * Enum with custom naming
                     */
                    public val bahEnum: ActionWithNonStringInputsV3.Bah
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
                            "bah-enum" to bahEnum.stringValue,
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

                    public sealed class Bah(
                        public val stringValue: String
                    ) {
                        public object HelloWorld : ActionWithNonStringInputsV3.Bah("helloworld")

                        public class Custom(
                            customStringValue: String
                        ) : ActionWithNonStringInputsV3.Bah(customStringValue)
                    }
                }

            """.trimIndent(),
            filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/ActionWithNonStringInputsV3.kt",
        )
    }

    test("action with outputs") {
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
            ),
            outputs = mapOf(
                "baz-goo" to Output(description = "Cool output!"),
                "loo-woz" to Output(description = "Another output..."),
            )
        )
        val coords = ActionCoords("john-smith", "action-with-outputs", "v3")

        // when
        val wrapper = coords.generateWrapper { actionManifest }
        writeToUnitTests(wrapper)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                // This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
                // be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
                // generator itself.
                package it.krzeminski.githubactions.actions.johnsmith

                import it.krzeminski.githubactions.actions.ActionWithOutputs
                import kotlin.String
                import kotlin.Suppress

                /**
                 * Action: Do something cool
                 *
                 * This is a test description that should be put in the KDoc comment for a class
                 *
                 * [Action on GitHub](https://github.com/john-smith/action-with-outputs)
                 */
                public class ActionWithOutputsV3(
                    /**
                     * Short description
                     */
                    public val fooBar: String
                ) : ActionWithOutputs<ActionWithOutputsV3.Outputs>("john-smith", "action-with-outputs", "v3") {
                    @Suppress("SpreadOperator")
                    public override fun toYamlArguments() = linkedMapOf(
                        *listOfNotNull(
                            "foo-bar" to fooBar,
                        ).toTypedArray()
                    )

                    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

                    public class Outputs(
                        private val stepId: String
                    ) {
                        /**
                         * Cool output!
                         */
                        public val bazGoo: String = "steps.${'$'}stepId.outputs.baz-goo"

                        /**
                         * Another output...
                         */
                        public val looWoz: String = "steps.${'$'}stepId.outputs.loo-woz"

                        public operator fun `get`(outputName: String) = "steps.${'$'}stepId.outputs.${'$'}outputName"
                    }
                }

            """.trimIndent(),
            filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/ActionWithOutputsV3.kt",
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
        val inputTypings = mapOf(
            "check-latest" to BooleanTyping,
            "foo-bar" to BooleanTyping,
            "bazGoo" to BooleanTyping,
        )
        val coords = ActionCoords("actions", "setup-node", "v2")

        shouldThrowAny {
            // when
            coords.generateWrapper(inputTypings) { actionManifest }
        }.shouldHaveMessage(
            // then
            """
            Request contains invalid properties:
            Available: [foo-bar, baz-goo]
            Invalid:   [check-latest, bazGoo]
            """.trimIndent()
        )
    }

    test("action with no inputs") {
        // given
        val actionManifestHasNoInputs = emptyMap<String, Input>()
        val actionManifest = Metadata(
            inputs = actionManifestHasNoInputs,
            name = "Action With No Inputs",
            description = "Description",
        )

        val coords = ActionCoords("john-smith", "action-with-no-inputs", "v3")

        // when
        val wrapper = coords.generateWrapper { actionManifest }
        writeToUnitTests(wrapper)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                // This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
                // be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
                // generator itself.
                package it.krzeminski.githubactions.actions.johnsmith

                import it.krzeminski.githubactions.actions.Action
                import java.util.LinkedHashMap
                import kotlin.Suppress

                /**
                 * Action: Action With No Inputs
                 *
                 * Description
                 *
                 * [Action on GitHub](https://github.com/john-smith/action-with-no-inputs)
                 */
                public class ActionWithNoInputsV3() : Action("john-smith", "action-with-no-inputs", "v3") {
                    @Suppress("SpreadOperator")
                    public override fun toYamlArguments() = LinkedHashMap<String, String>()
                }
                
            """.trimIndent(),
            filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/ActionWithNoInputsV3.kt",
        )
    }

    test("action v2 deprecated by v3") {
        // given
        val actionManifestHasNoInputs = emptyMap<String, Input>()
        val actionManifest = Metadata(
            inputs = actionManifestHasNoInputs,
            name = "Action With No Inputs",
            description = "Description",
        )

        val coords = ActionCoords("john-smith", "action-with-no-inputs", "v2", deprecatedByVersion = "v3")

        // when
        val wrapper = coords.generateWrapper { actionManifest }
        writeToUnitTests(wrapper)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                // This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
                // be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
                // generator itself.
                @file:Suppress("DEPRECATION")

                package it.krzeminski.githubactions.actions.johnsmith

                import it.krzeminski.githubactions.actions.Action
                import java.util.LinkedHashMap
                import kotlin.Deprecated
                import kotlin.Suppress

                /**
                 * Action: Action With No Inputs
                 *
                 * Description
                 *
                 * [Action on GitHub](https://github.com/john-smith/action-with-no-inputs)
                 */
                @Deprecated(
                    message = "This action has a newer major version",
                    replaceWith = ReplaceWith("ActionWithNoInputsV3")
                )
                public class ActionWithNoInputsV2() : Action("john-smith", "action-with-no-inputs", "v2") {
                    @Suppress("SpreadOperator")
                    public override fun toYamlArguments() = LinkedHashMap<String, String>()
                }

            """.trimIndent(),
            filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/ActionWithNoInputsV2.kt",
        )
    }

    test("action with ListOfTypings") {
        // given
        val actionManifest = Metadata(
            name = "Do something cool",
            description = "This is a test description that should be put in the KDoc comment for a class",
            inputs = mapOf(
                "list-strings" to Input("List of strings"),
                "list-ints" to Input("List of integers"),
                "list-enums" to Input("List of enums"),
                "list-int-special" to Input("List of integer with special values"),
            )
        )
        val inputTypings = mapOf(
            "list-strings" to ListOfTypings(",", StringTyping),
            "list-ints" to ListOfTypings(",", IntegerTyping),
            "list-enums" to ListOfTypings(
                delimiter = ",",
                typing = EnumTyping("MyEnum", listOf("one", "two", "three"))
            ),
            "list-int-special" to ListOfTypings(
                delimiter = ",",
                typing = IntegerWithSpecialValueTyping("MyInt", mapOf("the-answer" to 42))
            )
        )
        val coords = ActionCoords("john-smith", "simple-action-with-lists", "v3")

        // when
        val wrapper = coords.generateWrapper(inputTypings) { actionManifest }
        writeToUnitTests(wrapper)

        // then
        wrapper shouldBe Wrapper(
            kotlinCode = """
                // This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
                // be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
                // generator itself.
                package it.krzeminski.githubactions.actions.johnsmith

                import it.krzeminski.githubactions.actions.Action
                import kotlin.Int
                import kotlin.String
                import kotlin.Suppress
                import kotlin.collections.List

                /**
                 * Action: Do something cool
                 *
                 * This is a test description that should be put in the KDoc comment for a class
                 *
                 * [Action on GitHub](https://github.com/john-smith/simple-action-with-lists)
                 */
                public class SimpleActionWithListsV3(
                    /**
                     * List of strings
                     */
                    public val listStrings: List<String>? = null,
                    /**
                     * List of integers
                     */
                    public val listInts: List<Int>? = null,
                    /**
                     * List of enums
                     */
                    public val listEnums: List<SimpleActionWithListsV3.MyEnum>? = null,
                    /**
                     * List of integer with special values
                     */
                    public val listIntSpecial: List<SimpleActionWithListsV3.MyInt>? = null
                ) : Action("john-smith", "simple-action-with-lists", "v3") {
                    @Suppress("SpreadOperator")
                    public override fun toYamlArguments() = linkedMapOf(
                        *listOfNotNull(
                            listStrings?.let { "list-strings" to it.joinToString(",") },
                            listInts?.let { "list-ints" to it.joinToString(",") { it.toString() } },
                            listEnums?.let { "list-enums" to it.joinToString(",") { it.stringValue } },
                            listIntSpecial?.let { "list-int-special" to it.joinToString(",") {
                                    it.integerValue.toString() } },
                        ).toTypedArray()
                    )

                    public sealed class MyEnum(
                        public val stringValue: String
                    ) {
                        public object One : SimpleActionWithListsV3.MyEnum("one")

                        public object Two : SimpleActionWithListsV3.MyEnum("two")

                        public object Three : SimpleActionWithListsV3.MyEnum("three")

                        public class Custom(
                            customStringValue: String
                        ) : SimpleActionWithListsV3.MyEnum(customStringValue)
                    }

                    public sealed class MyInt(
                        public val integerValue: Int
                    ) {
                        public class Value(
                            requestedValue: Int
                        ) : SimpleActionWithListsV3.MyInt(requestedValue)

                        public object TheAnswer : SimpleActionWithListsV3.MyInt(42)
                    }
                }

            """.trimIndent(),
            filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/SimpleActionWithListsV3.kt",
        )
    }
})
