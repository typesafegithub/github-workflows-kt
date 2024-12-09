package io.github.typesafegithub.workflows.actionbindinggenerator.typing

import com.charleskorn.kaml.ForbiddenAnchorOrAliasException
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.IOException
import java.net.URI

class TypesProvidingTest :
    FunSpec({
        test("parses all allowed elements of valid typing") {
            // Given
            val actionTypesYml =
                """
                inputs:
                  name:
                    type: string
                  verbose:
                    type: boolean
                  retries:
                    type: integer
                  some-float:
                    type: float
                  fetch-depth:
                    type: integer
                    named-values:
                      infinite: 0
                  input-files:
                    type: list
                    separator: ','
                    list-item:
                      type: string
                  granted-scopes:
                    type: list
                    separator: ','
                    list-item:
                      type: enum
                      allowed-values:
                        - read
                        - write
                  permissions:
                    type: enum
                    allowed-values:
                      - user
                      - admin
                      - guest
                """.trimIndent()
            val actionCoord = ActionCoords("some-owner", "some-name", "v1")

            // When
            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), { actionTypesYml })

            // Then
            types.first shouldBe
                mapOf(
                    "name" to StringTyping,
                    "verbose" to BooleanTyping,
                    "retries" to IntegerTyping,
                    "some-float" to FloatTyping,
                    "fetch-depth" to IntegerWithSpecialValueTyping("FetchDepth", mapOf("Infinite" to 0)),
                    "input-files" to ListOfTypings(","),
                    "granted-scopes" to ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                    "permissions" to EnumTyping("Permissions", listOf("user", "admin", "guest")),
                )
        }

        context("order of using typings from various sources") {
            val hostedByActionYml =
                """
                inputs:
                  hosted-by-action-yml:
                    type: string
                """.trimIndent()
            val hostedByActionYaml =
                """
                inputs:
                  hosted-by-action-yaml:
                    type: string
                """.trimIndent()
            val storedInTypingCatalog =
                """
                inputs:
                  stored-in-typing-catalog:
                    type: string
                """.trimIndent()
            val metadata =
                """
                "versionsWithTypings":
                - "v2"
                - "v3"
                - "v4"
                """.trimIndent()
            val storedInTypingCatalogForOlderVersion =
                """
                inputs:
                  stored-in-typing-catalog-for-older-version:
                    type: string
                """.trimIndent()

            test("only hosted by the action (.yml)") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yml") -> hostedByActionYml
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("hosted-by-action-yml" to StringTyping), TypingActualSource.ACTION)
            }

            test("only hosted by the subaction (.yml)") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/some-owner/some-name/some-hash/some-sub/action-types.yml",
                        ),
                        -> hostedByActionYml
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("hosted-by-action-yml" to StringTyping), TypingActualSource.ACTION)
            }

            test("only hosted by the action (.yaml)") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yaml") -> hostedByActionYaml
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("hosted-by-action-yaml" to StringTyping), TypingActualSource.ACTION)
            }

            test("only hosted by the subaction (.yaml)") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/some-owner/some-name/some-hash/some-sub/action-types.yaml",
                        ),
                        -> hostedByActionYaml
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("hosted-by-action-yaml" to StringTyping), TypingActualSource.ACTION)
            }

            test("only hosted by the action, both extensions") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yml") -> hostedByActionYml
                        URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yaml") -> hostedByActionYaml
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("hosted-by-action-yml" to StringTyping), TypingActualSource.ACTION)
            }

            test("only hosted by the subaction, both extensions") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/some-owner/some-name/some-hash/some-sub/action-types.yml",
                        ),
                        -> hostedByActionYml
                        URI(
                            "https://raw.githubusercontent.com/some-owner/some-name/some-hash/some-sub/action-types.yaml",
                        ),
                        -> hostedByActionYaml
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("hosted-by-action-yml" to StringTyping), TypingActualSource.ACTION)
            }

            test("only stored in typing catalog") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/action-types.yml",
                        ),
                        -> storedInTypingCatalog
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("stored-in-typing-catalog" to StringTyping), TypingActualSource.TYPING_CATALOG)
            }

            test("only stored in typing catalog for subaction") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/some-sub/action-types.yml",
                        ),
                        -> storedInTypingCatalog
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("stored-in-typing-catalog" to StringTyping), TypingActualSource.TYPING_CATALOG)
            }

            test("hosted by action and stored in typing catalog") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/some-owner/some-name/" +
                                "some-hash/action-types.yml",
                        ),
                        -> hostedByActionYml
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/action-types.yml",
                        ),
                        -> storedInTypingCatalog
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("hosted-by-action-yml" to StringTyping), TypingActualSource.ACTION)
            }

            test("hosted by subaction and stored in typing catalog") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/some-owner/some-name/" +
                                "some-hash/some-sub/action-types.yml",
                        ),
                        -> hostedByActionYml
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/some-sub/action-types.yml",
                        ),
                        -> storedInTypingCatalog
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("hosted-by-action-yml" to StringTyping), TypingActualSource.ACTION)
            }

            test("only stored in typing catalog for older version") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/metadata.yml",
                        ),
                        -> metadata
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v4/action-types.yml",
                        ),
                        -> storedInTypingCatalogForOlderVersion
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v6")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("stored-in-typing-catalog-for-older-version" to StringTyping), TypingActualSource.TYPING_CATALOG)
            }

            test("only stored in typing catalog for older version of subaction") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/metadata.yml",
                        ),
                        -> metadata
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v4/some-sub/action-types.yml",
                        ),
                        -> storedInTypingCatalogForOlderVersion
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v6", FULL, "some-sub")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(mapOf("stored-in-typing-catalog-for-older-version" to StringTyping), TypingActualSource.TYPING_CATALOG)
            }

            test("metadata available but no version available") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/metadata.yml",
                        ),
                        -> metadata
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v1")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(emptyMap(), null)
            }

            test("no typings at all") {
                // Given
                val fetchUri: (URI) -> String = { throw IOException() }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe Pair(emptyMap(), null)
            }
        }

        context("YAML anchors and aliases") {
            val typingYml =
                """
                inputs:
                  granted-scopes: &scopes-list
                    type: list
                    separator: ','
                    list-item:
                      type: enum
                      name: GrantedScopes
                      allowed-values:
                        - read
                        - write
                  granted-scopes2: *scopes-list
                  granted-scopes3:
                    <<: *scopes-list
                    separator: '\n'
                """.trimIndent()
            val metadata =
                """
                "versionsWithTypings": &versions
                - "v2"
                - "v3"
                - "v4"
                """.trimIndent()

            test("hosted by the action") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yml") -> typingYml
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe
                    Pair(
                        mapOf(
                            "granted-scopes" to ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                            "granted-scopes2" to ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                            "granted-scopes3" to ListOfTypings("""\n""", EnumTyping("GrantedScopes", listOf("read", "write"))),
                        ),
                        TypingActualSource.ACTION,
                    )
            }

            test("hosted by typing catalog") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/action-types.yml",
                        ),
                        -> typingYml
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe
                    Pair(
                        mapOf(
                            "granted-scopes" to ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                            "granted-scopes2" to ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                            "granted-scopes3" to ListOfTypings("""\n""", EnumTyping("GrantedScopes", listOf("read", "write"))),
                        ),
                        TypingActualSource.TYPING_CATALOG,
                    )
            }

            test("only stored in typing catalog for older version") {
                // Given
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/metadata.yml",
                        ),
                        -> metadata
                        URI(
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v4/action-types.yml",
                        ),
                        -> typingYml
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v6")

                // When
                val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

                // Then
                types shouldBe
                    Pair(
                        mapOf(
                            "granted-scopes" to ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                            "granted-scopes2" to ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                            "granted-scopes3" to ListOfTypings("""\n""", EnumTyping("GrantedScopes", listOf("read", "write"))),
                        ),
                        TypingActualSource.TYPING_CATALOG,
                    )
            }

            test("billion laughs attack is prevented") {
                // Given
                val billionLaughsAttack =
                    """
                    a: &a ["lol","lol","lol","lol","lol","lol","lol","lol","lol"]
                    b: &b [*a,*a,*a,*a,*a,*a,*a,*a,*a]
                    c: &c [*b,*b,*b,*b,*b,*b,*b,*b,*b]
                    d: &d [*c,*c,*c,*c,*c,*c,*c,*c,*c]
                    e: &e [*d,*d,*d,*d,*d,*d,*d,*d,*d]
                    f: &f [*e,*e,*e,*e,*e,*e,*e,*e,*e]
                    g: &g [*f,*f,*f,*f,*f,*f,*f,*f,*f]
                    h: &h [*g,*g,*g,*g,*g,*g,*g,*g,*g]
                    i: &i [*h,*h,*h,*h,*h,*h,*h,*h,*h]
                    """.trimIndent()
                val fetchUri: (URI) -> String = {
                    when (it) {
                        URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yml") -> billionLaughsAttack
                        else -> throw IOException()
                    }
                }
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // Expect
                val exception =
                    shouldThrow<ForbiddenAnchorOrAliasException> {
                        actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)
                    }
                exception.message shouldBe "Maximum number of aliases has been reached."
            }
        }

        test("non-numeric version is provided and metadata in typing catalog exists") {
            // Given
            val metadata =
                """
                "versionsWithTypings":
                - "v2"
                - "v3"
                - "v4"
                """.trimIndent()
            val fetchUri: (URI) -> String = {
                when (it) {
                    URI(
                        "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                            "main/typings/some-owner/some-name/metadata.yml",
                    ),
                    -> metadata
                    else -> throw IOException()
                }
            }
            val actionCoord = ActionCoords("some-owner", "some-name", "v6-beta")

            // When
            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)

            // Then
            types shouldBe Pair(emptyMap(), null)
        }
    })
