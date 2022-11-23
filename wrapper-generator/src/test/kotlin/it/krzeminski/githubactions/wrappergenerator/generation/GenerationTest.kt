package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
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
            name = """
                Do something cool
                and describe it in multiple lines
            """.trimIndent(),
            description = "This is a test description that should be put in the KDoc comment for a class",
            inputs = mapOf(
                "foo-bar" to Input(
                    description = "Short description",
                    required = true,
                    default = null,
                ),
                "baz-goo" to Input(
                    description = """
                        Just another input
                        with multiline description
                    """.trimIndent(),
                    deprecationMessage = "this is deprecated",
                    required = true,
                    default = null,
                ),
            ),
        )
        val coords = ActionCoords("john-smith", "simple-action-with-required-string-inputs", "v3")

        // when
        val wrapper = coords.generateWrapper { actionManifest }

        // then
        wrapper.shouldMatchFile("SimpleActionWithRequiredStringInputsV3.kt")
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
            ),
        )
        val coords = ActionCoords("john-smith", "action-with-some-optional-inputs", "v3")

        // when
        val wrapper = coords.generateWrapper(fetchMetadataImpl = { actionManifest })

        // then
        wrapper.shouldMatchFile("ActionWithSomeOptionalInputsV3.kt")
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
            ),
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

        // then
        wrapper.shouldMatchFile("ActionWithNonStringInputsV3.kt")
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
            ),
        )
        val coords = ActionCoords("john-smith", "action-with-outputs", "v3")

        // when
        val wrapper = coords.generateWrapper { actionManifest }

        // then
        wrapper.shouldMatchFile("ActionWithOutputsV3.kt")
    }

    test("Detect wrapper request with invalid properties") {
        // given
        val input = Input("input", "default", required = true)
        val actionManifest = Metadata(
            name = "Do something cool",
            description = "This is a test description that should be put in the KDoc comment for a class",
            inputs = mapOf(
                "foo-bar" to input,
                "baz-goo" to input,
            ),
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
            """.trimIndent(),
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

        // then
        wrapper.shouldMatchFile("ActionWithNoInputsV3.kt")
    }

    test("action v2 deprecated by v3") {
        // given
        val actionManifestHasNoInputs = emptyMap<String, Input>()
        val actionManifest = Metadata(
            inputs = actionManifestHasNoInputs,
            name = "Deprecated Action",
            description = "Description",
        )

        val coords = ActionCoords("john-smith", "deprecated-action", "v2", deprecatedByVersion = "v3")

        // when
        val wrapper = coords.generateWrapper { actionManifest }

        // then
        wrapper.shouldMatchFile("DeprecatedActionV2.kt")
    }

    test("action with deprecated input resolving to the same Kotlin field name") {
        // given
        val actionManifest = Metadata(
            inputs = mapOf(
                "foo-bar" to Input(
                    description = "Foo bar - old",
                    required = true,
                    default = null,
                    deprecationMessage = "Use 'fooBar'!",
                ),
                "fooBar" to Input(
                    description = "Foo bar - new",
                    required = true,
                    default = null,
                ),
            ),
            name = "Some Action",
            description = "Description",
        )

        val coords = ActionCoords("john-smith", "action-with-deprecated-input-and-name-clash", "v2")

        // when
        val wrapper = coords.generateWrapper { actionManifest }

        // then
        wrapper.shouldMatchFile("ActionWithDeprecatedInputAndNameClashV2.kt")
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
            ),
        )
        val inputTypings = mapOf(
            "list-strings" to ListOfTypings(",", StringTyping),
            "list-ints" to ListOfTypings(",", IntegerTyping),
            "list-enums" to ListOfTypings(
                delimiter = ",",
                typing = EnumTyping("MyEnum", listOf("one", "two", "three")),
            ),
            "list-int-special" to ListOfTypings(
                delimiter = ",",
                typing = IntegerWithSpecialValueTyping("MyInt", mapOf("the-answer" to 42)),
            ),
        )
        val coords = ActionCoords("john-smith", "simple-action-with-lists", "v3")

        // when
        val wrapper = coords.generateWrapper(inputTypings) { actionManifest }

        // then
        wrapper.shouldMatchFile("SimpleActionWithListsV3.kt")
    }
},)
