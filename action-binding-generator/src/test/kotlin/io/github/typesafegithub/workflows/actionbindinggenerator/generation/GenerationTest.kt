package io.github.typesafegithub.workflows.actionbindinggenerator.generation

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MAJOR
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MINOR
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource.ACTION
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource.TYPING_CATALOG
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Input
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Metadata
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Output
import io.github.typesafegithub.workflows.actionbindinggenerator.shouldContainAndMatchFile
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.BooleanTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.EnumTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.FloatTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.IntegerTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.IntegerWithSpecialValueTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.ListOfTypings
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.StringTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.Typing
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize

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
                "foo-bar" to StringTyping,
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
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(actionManifest.allInputsAsStrings(), ACTION),
                )

            // then
            binding.shouldContainAndMatchFile("SimpleActionWithRequiredStringInputs.kt")
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
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(actionManifest.allInputsAsStrings(), ACTION),
                )

            // then
            binding.shouldContainAndMatchFile("ActionWithSomeOptionalInputs.kt")
        }

        test("action with all types of inputs") {
            // given
            val coords = ActionCoords("john-smith", "action-with-all-types-of-inputs", "v3")

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifestWithAllTypesOfInputsAndSomeOutput,
                    inputTypings =
                        Pair(
                            typingsForAllTypesOfInputs,
                            ACTION,
                        ),
                )

            // then
            assertSoftly {
                binding.shouldContainAndMatchFile("ActionWithAllTypesOfInputs.kt")
                binding.shouldContainAndMatchFile("ActionWithAllTypesOfInputs_Untyped.kt")
            }
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
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(actionManifest.allInputsAsStrings(), ACTION),
                )

            // then
            binding.shouldContainAndMatchFile("ActionWithOutputs.kt")
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
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(emptyMap(), ACTION),
                )

            // then
            binding.shouldContainAndMatchFile("ActionWithNoInputs.kt")
        }

        test("subaction") {
            // given
            val actionManifestHasNoInputs = emptyMap<String, Input>()
            val actionManifest =
                Metadata(
                    inputs = actionManifestHasNoInputs,
                    name = "Action With No Inputs",
                    description = "Description",
                )

            val coords = ActionCoords("john-smith", "action-with", "v3", FULL, "sub/action")

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(emptyMap(), ACTION),
                )

            // then
            binding.shouldContainAndMatchFile("ActionWithSubAction.kt")
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
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(actionManifest.allInputsAsStrings(), ACTION),
                )

            // then
            binding.shouldContainAndMatchFile("ActionWithDeprecatedInputAndNameClash.kt")
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
                    metadataRevision = NewestForVersion,
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
            binding.shouldContainAndMatchFile("ActionWithInputsSharingType.kt")
        }

        test("action with input descriptions with fancy characters") {
            // given
            val actionManifest =
                Metadata(
                    name = "Do something cool",
                    description = "This is a test description that should be put in the KDoc comment for a class",
                    inputs =
                        mapOf(
                            "nested-kotlin-comments" to
                                Input(
                                    description = "This is a /* test */",
                                ),
                            "percent" to
                                Input(
                                    description = "For example \"100%\"",
                                ),
                        ),
                )
            val coords = ActionCoords("john-smith", "action-with-fancy-chars-in-docs", "v3")

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(actionManifest.allInputsAsStrings(), ACTION),
                )

            // then
            binding.shouldContainAndMatchFile("ActionWithFancyCharsInDocs.kt")
        }

        test("action with no typings has only an untyped binding") {
            // given
            val actionManifest =
                Metadata(
                    name = "Do something cool",
                    description = "This is a test description that should be put in the KDoc comment for a class",
                    inputs =
                        mapOf(
                            "foo" to
                                Input(
                                    required = true,
                                    default = null,
                                ),
                            "bar" to
                                Input(
                                    required = false,
                                    default = "test",
                                ),
                        ),
                )
            val coords = ActionCoords("john-smith", "action-with-no-typings", "v3")

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(emptyMap(), null),
                )

            // then
            binding shouldHaveSize 1
            binding.shouldContainAndMatchFile("ActionWithNoTypings_Untyped.kt")
        }

        test("action with partly typings has only untyped properties for the non-typed inputs") {
            // given
            val actionManifest =
                Metadata(
                    name = "Do something cool",
                    description = "This is a test description that should be put in the KDoc comment for a class",
                    inputs =
                        mapOf(
                            "foo" to
                                Input(
                                    required = true,
                                    default = null,
                                ),
                            "bar" to
                                Input(
                                    required = false,
                                    default = "test",
                                ),
                            "baz" to
                                Input(
                                    required = true,
                                ),
                        ),
                )
            val coords = ActionCoords("john-smith", "action-with-partly-typings", "v3")

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(mapOf("foo" to IntegerTyping), TYPING_CATALOG),
                )

            // then
            assertSoftly {
                binding.shouldContainAndMatchFile("ActionWithPartlyTypings.kt")
                binding.shouldContainAndMatchFile("ActionWithPartlyTypings_Untyped.kt")
            }
        }

        test("action with no inputs with major version") {
            // given
            val actionManifestHasNoInputs = emptyMap<String, Input>()
            val actionManifest =
                Metadata(
                    inputs = actionManifestHasNoInputs,
                    name = "Action With No Inputs",
                    description = "Description",
                )

            val coords = ActionCoords("john-smith", "action-with-no-inputs-with-major-version", "v3.1.3", MAJOR)

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(emptyMap(), ACTION),
                )

            // then
            assertSoftly {
                binding.shouldContainAndMatchFile("ActionWithNoInputsWithMajorVersion.kt")
                binding.shouldContainAndMatchFile("ActionWithNoInputsWithMajorVersion_Untyped.kt")
            }
        }

        test("action with no inputs with minor version") {
            // given
            val actionManifestHasNoInputs = emptyMap<String, Input>()
            val actionManifest =
                Metadata(
                    inputs = actionManifestHasNoInputs,
                    name = "Action With No Inputs",
                    description = "Description",
                )

            val coords = ActionCoords("john-smith", "action-with-no-inputs-with-minor-version", "v3.1.3", MINOR)

            // when
            val binding =
                coords.generateBinding(
                    metadataRevision = NewestForVersion,
                    metadata = actionManifest,
                    inputTypings = Pair(emptyMap(), ACTION),
                )

            // then
            assertSoftly {
                binding.shouldContainAndMatchFile("ActionWithNoInputsWithMinorVersion.kt")
                binding.shouldContainAndMatchFile("ActionWithNoInputsWithMinorVersion_Untyped.kt")
            }
        }
    })

private fun Metadata.allInputsAsStrings(): Map<String, Typing> = this.inputs.mapValues { StringTyping }
