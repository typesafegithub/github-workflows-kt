package io.github.typesafegithub.workflows.actionbindinggenerator.generation

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.FromLockfile
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource.ACTION
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Input
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Metadata
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Output
import io.github.typesafegithub.workflows.actionbindinggenerator.shouldMatchFile
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.BooleanTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.EnumTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.FloatTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.IntegerTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.IntegerWithSpecialValueTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.ListOfTypings
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.StringTyping
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe

class GenerationTest :
    FunSpec({
        val actionManifestWithAllTypesOfInputsAndSomeOutput =
            Metadata(
                name = "Do something cool",
                description = "This is a test description that should be put in the KDoc comment for a class",
                inputs =
                    mapOf(
                        "foo-bar" to
                            Input(
                                description = "Short description",
                                required = true,
                                default = null,
                            ),
                        "baz-goo" to
                            Input(
                                description = "First boolean input!",
                                required = true,
                                default = null,
                            ),
                        "bin-kin" to
                            Input(
                                description = "Boolean and nullable",
                                required = false,
                                default = "test",
                            ),
                        "int-pint" to
                            Input(
                                description = "Integer",
                                required = true,
                                default = null,
                            ),
                        "flo-pint" to
                            Input(
                                description = "Float",
                                required = true,
                                default = null,
                            ),
                        "fin-bin" to
                            Input(
                                description = "Enumeration",
                                required = true,
                                default = null,
                            ),
                        "goo-zen" to
                            Input(
                                description = "Integer with special value",
                                required = true,
                                default = null,
                            ),
                        "bah-enum" to
                            Input(
                                description = "Enum with custom naming",
                                required = true,
                                default = null,
                            ),
                        "list-strings" to Input("List of strings"),
                        "list-ints" to Input("List of integers"),
                        "list-enums" to Input("List of enums"),
                        "list-int-special" to Input("List of integer with special values"),
                    ),
                outputs =
                    mapOf(
                        "baz-goo" to Output(description = "Cool output!"),
                    ),
            )
        val typingsForAllTypesOfInputs =
            mapOf(
                "baz-goo" to BooleanTyping,
                "bin-kin" to BooleanTyping,
                "int-pint" to IntegerTyping,
                "flo-pint" to FloatTyping,
                "fin-bin" to EnumTyping("Bin", listOf("foo", "boo-bar", "baz123")),
                "goo-zen" to IntegerWithSpecialValueTyping("Zen", mapOf("Special1" to 3, "Special2" to -1)),
                "bah-enum" to EnumTyping(null, listOf("helloworld"), listOf("HelloWorld")),
                "list-strings" to ListOfTypings(",", StringTyping),
                "list-ints" to ListOfTypings(",", IntegerTyping),
                "list-enums" to
                    ListOfTypings(
                        delimiter = ",",
                        typing = EnumTyping("MyEnum", listOf("one", "two", "three")),
                    ),
                "list-int-special" to
                    ListOfTypings(
                        delimiter = ",",
                        typing = IntegerWithSpecialValueTyping("MyInt", mapOf("the-answer" to 42)),
                    ),
            )

        test("action with required inputs as strings, no outputs") {
            // given
            val actionManifest =
                Metadata(
                    name =
                        """
                        Do something cool
                        and describe it in multiple lines
                        """.trimIndent(),
                    description = "This is a test description that should be put in the KDoc comment for a class",
                    inputs =
                        mapOf(
                            "foo-bar" to
                                Input(
                                    description = "Short description",
                                    required = true,
                                    default = null,
                                ),
                            "baz-goo" to
                                Input(
                                    description =
                                        """
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
            val binding = coords.generateBinding(metadataRevision = FromLockfile, metadata = actionManifest)

            // then
            binding shouldNotBe null
            binding?.shouldMatchFile("SimpleActionWithRequiredStringInputsV3.kt")
        }

        test("action with various combinations of input parameters describing being required or optional") {
            // given
            val actionManifest =
                Metadata(
                    name = "Do something cool",
                    description = "This is a test description that should be put in the KDoc comment for a class",
                    inputs =
                        mapOf(
                            "foo-bar" to
                                Input(
                                    description = "Required is default, default is set",
                                    default = "barfoo",
                                ),
                            "baz-goo" to
                                Input(
                                    description = "Required is default, default is null",
                                    default = null,
                                ),
                            "zoo-dar" to
                                Input(
                                    description = "Required is false, default is set",
                                    required = false,
                                    default = "googoo",
                                ),
                            "coo-poo" to
                                Input(
                                    description = "Required is false, default is default",
                                    required = false,
                                ),
                            "package" to
                                Input(
                                    description = "Required is true, default is default",
                                    required = true,
                                ),
                        ),
                )
            val coords = ActionCoords("john-smith", "action-with-some-optional-inputs", "v3")

            // when
            val binding = coords.generateBinding(metadataRevision = FromLockfile, metadata = actionManifest)

            // then
            binding shouldNotBe null
            binding?.shouldMatchFile("ActionWithSomeOptionalInputsV3.kt")
        }

        test("action with all types of inputs") {
            // given
            val coords = ActionCoords("john-smith", "action-with-all-types-of-inputs", "v3")

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = FromLockfile,
                    metadata = actionManifestWithAllTypesOfInputsAndSomeOutput,
                    inputTypings =
                        Pair(
                            typingsForAllTypesOfInputs,
                            ACTION,
                        ),
                )

            // then
            binding shouldNotBe null
            binding?.shouldMatchFile("ActionWithAllTypesOfInputsV3.kt")
        }

        test("action with outputs") {
            // given
            val actionManifest =
                Metadata(
                    name = "Do something cool",
                    description = "This is a test description that should be put in the KDoc comment for a class",
                    inputs =
                        mapOf(
                            "foo-bar" to
                                Input(
                                    description = "Short description",
                                    required = true,
                                    default = null,
                                ),
                        ),
                    outputs =
                        mapOf(
                            "baz-goo" to Output(description = "Cool output!"),
                            "loo-woz" to Output(description = "Another output..."),
                        ),
                )
            val coords = ActionCoords("john-smith", "action-with-outputs", "v3")

            // when
            val binding = coords.generateBinding(metadataRevision = FromLockfile, metadata = actionManifest)

            // then
            binding shouldNotBe null
            binding?.shouldMatchFile("ActionWithOutputsV3.kt")
        }

        test("action with no inputs") {
            // given
            val actionManifestHasNoInputs = emptyMap<String, Input>()
            val actionManifest =
                Metadata(
                    inputs = actionManifestHasNoInputs,
                    name = "Action With No Inputs",
                    description = "Description",
                )

            val coords = ActionCoords("john-smith", "action-with-no-inputs", "v3")

            // when
            val binding = coords.generateBinding(metadataRevision = FromLockfile, metadata = actionManifest)

            // then
            binding shouldNotBe null
            binding?.shouldMatchFile("ActionWithNoInputsV3.kt")
        }

        test("action v2 deprecated by v3") {
            // given
            val actionManifestHasNoInputs = emptyMap<String, Input>()
            val actionManifest =
                Metadata(
                    inputs = actionManifestHasNoInputs,
                    name = "Deprecated Action",
                    description = "Description",
                )

            val coords = ActionCoords("john-smith", "deprecated-action", "v2", deprecatedByVersion = "v3")

            // when
            val binding = coords.generateBinding(metadataRevision = FromLockfile, metadata = actionManifest)

            // then
            binding?.shouldMatchFile("DeprecatedActionV2.kt")
        }

        test("action with deprecated input resolving to the same Kotlin field name") {
            // given
            val actionManifest =
                Metadata(
                    inputs =
                        mapOf(
                            "foo-bar" to
                                Input(
                                    description = "Foo bar - old",
                                    required = true,
                                    default = null,
                                    deprecationMessage = "Use 'fooBar'!",
                                ),
                            "fooBar" to
                                Input(
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
            val binding = coords.generateBinding(metadataRevision = FromLockfile, metadata = actionManifest)

            // then
            binding shouldNotBe null
            binding?.shouldMatchFile("ActionWithDeprecatedInputAndNameClashV2.kt")
        }

        test("action with inputs sharing type") {
            // given
            val actionManifest =
                Metadata(
                    name = "Do something cool",
                    description = "This is a test description that should be put in the KDoc comment for a class",
                    inputs =
                        mapOf(
                            "foo-one" to
                                Input(
                                    required = true,
                                    default = null,
                                ),
                            "foo-two" to
                                Input(
                                    required = true,
                                    default = null,
                                ),
                            "foo-three" to
                                Input(
                                    required = false,
                                    default = "test",
                                ),
                        ),
                )
            val coords = ActionCoords("john-smith", "action-with-inputs-sharing-type", "v3")

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = FromLockfile,
                    metadata = actionManifest,
                    inputTypings =
                        Pair(
                            mapOf(
                                "foo-one" to IntegerWithSpecialValueTyping("Foo", mapOf("Special1" to 3)),
                                "foo-two" to IntegerWithSpecialValueTyping("Foo", mapOf("Special1" to 3)),
                                "foo-three" to IntegerWithSpecialValueTyping("Foo", mapOf("Special1" to 3)),
                            ),
                            ACTION,
                        ),
                )

            // then
            binding shouldNotBe null
            binding?.shouldMatchFile("ActionWithInputsSharingTypeV3.kt")
        }

        test("action binding generated for the versioned JAR") {
            // given
            val coords = ActionCoords("john-smith", "action-for-generated-jar", "v3")

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = FromLockfile,
                    metadata = actionManifestWithAllTypesOfInputsAndSomeOutput,
                    clientType = ClientType.VERSIONED_JAR,
                    inputTypings =
                        Pair(
                            typingsForAllTypesOfInputs,
                            ACTION,
                        ),
                )

            // then
            binding shouldNotBe null
            binding?.shouldMatchFile("ActionForGeneratedJar.kt")
        }
    })
