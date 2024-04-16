package io.github.typesafegithub.workflows.actionbindinggenerator.typing

import io.kotest.core.spec.style.FunSpec

class TypesProvidingTest : FunSpec({
//    test("parses all allowed elements of valid typing") {
//        // Given
//        val actionTypesYml =
//            """
//            inputs:
//              name:
//                type: string
//              verbose:
//                type: boolean
//              retries:
//                type: integer
//              some-float:
//                type: float
//              fetch-depth:
//                type: integer
//                named-values:
//                  infinite: 0
//              input-files:
//                type: list
//                separator: ','
//                list-item:
//                  type: string
//              granted-scopes:
//                type: list
//                separator: ','
//                list-item:
//                  type: enum
//                  allowed-values:
//                    - read
//                    - write
//              permissions:
//                type: enum
//                allowed-values:
//                  - user
//                  - admin
//                  - guest
//            """.trimIndent()
//        val actionCoord = ActionCoords("some-owner", "some-name", "v1")
//
//        // When
//        val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), { actionTypesYml })
//
//        // Then
//        types.first shouldBe
//            mapOf(
//                "name" to StringTyping,
//                "verbose" to BooleanTyping,
//                "retries" to IntegerTyping,
//                "some-float" to FloatTyping,
//                "fetch-depth" to IntegerWithSpecialValueTyping("FetchDepth", mapOf("Infinite" to 0)),
//                "input-files" to ListOfTypings(","),
//                "granted-scopes" to ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
//                "permissions" to EnumTyping("Permissions", listOf("user", "admin", "guest")),
//            )
//    }
//
//    context("order of using typings from various sources") {
//        val hostedByActionYml =
//            """
//            inputs:
//              hosted-by-action-yml:
//                type: string
//            """.trimIndent()
//        val hostedByActionYaml =
//            """
//            inputs:
//              hosted-by-action-yaml:
//                type: string
//            """.trimIndent()
//        val storedInTypingCatalog =
//            """
//            inputs:
//              stored-in-typing-catalog:
//                type: string
//            """.trimIndent()
//        val metadata =
//            """
//            "versionsWithTypings":
//            - "v2"
//            - "v3"
//            - "v4"
//            """.trimIndent()
//        val storedInTypingCatalogForOlderVersion =
//            """
//            inputs:
//              stored-in-typing-catalog-for-older-version:
//                type: string
//            """.trimIndent()
//
//        test("only hosted by the action (.yml)") {
//            // Given
//            val fetchUri: (URI) -> String = {
//                when (it) {
//                    URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash//action-types.yml") -> hostedByActionYml
//                    else -> throw IOException()
//                }
//            }
//            val actionCoord = ActionCoords("some-owner", "some-name", "v3")
//
//            // When
//            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)
//
//            // Then
//            types shouldBe Pair(mapOf("hosted-by-action-yml" to StringTyping), TypingActualSource.ACTION)
//        }
//
//        test("only hosted by the action (.yaml)") {
//            // Given
//            val fetchUri: (URI) -> String = {
//                when (it) {
//                    URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash//action-types.yaml") -> hostedByActionYaml
//                    else -> throw IOException()
//                }
//            }
//            val actionCoord = ActionCoords("some-owner", "some-name", "v3")
//
//            // When
//            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)
//
//            // Then
//            types shouldBe Pair(mapOf("hosted-by-action-yaml" to StringTyping), TypingActualSource.ACTION)
//        }
//
//        test("only hosted by the action, both extensions") {
//            // Given
//            val fetchUri: (URI) -> String = {
//                when (it) {
//                    URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash//action-types.yml") -> hostedByActionYml
//                    URI("https://raw.githubusercontent.com/some-owner/some-name/some-hash//action-types.yaml") -> hostedByActionYaml
//                    else -> throw IOException()
//                }
//            }
//            val actionCoord = ActionCoords("some-owner", "some-name", "v3")
//
//            // When
//            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)
//
//            // Then
//            types shouldBe Pair(mapOf("hosted-by-action-yml" to StringTyping), TypingActualSource.ACTION)
//        }
//
//        test("only stored in typing catalog") {
//            // Given
//            val fetchUri: (URI) -> String = {
//                when (it) {
//                    URI(
//                        "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
//                            "main/typings/some-owner/some-name/v3//action-types.yml",
//                    ),
//                    -> storedInTypingCatalog
//                    else -> throw IOException()
//                }
//            }
//            val actionCoord = ActionCoords("some-owner", "some-name", "v3")
//
//            // When
//            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)
//
//            // Then
//            types shouldBe Pair(mapOf("stored-in-typing-catalog" to StringTyping), TypingActualSource.TYPING_CATALOG)
//        }
//
//        test("hosted by action and stored in typing catalog") {
//            // Given
//            val fetchUri: (URI) -> String = {
//                when (it) {
//                    URI(
//                        "https://raw.githubusercontent.com/some-owner/some-name/" +
//                            "some-hash//action-types.yml",
//                    ),
//                    -> hostedByActionYml
//                    URI(
//                        "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
//                            "main/typings/some-owner/some-name/v3//action-types.yml",
//                    ),
//                    -> storedInTypingCatalog
//                    else -> throw IOException()
//                }
//            }
//            val actionCoord = ActionCoords("some-owner", "some-name", "v3")
//
//            // When
//            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)
//
//            // Then
//            types shouldBe Pair(mapOf("hosted-by-action-yml" to StringTyping), TypingActualSource.ACTION)
//        }
//
//        test("only stored in typing catalog for older version") {
//            // Given
//            val fetchUri: (URI) -> String = {
//                when (it) {
//                    URI(
//                        "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
//                            "main/typings/some-owner/some-name/metadata.yml",
//                    ),
//                    -> metadata
//                    URI(
//                        "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
//                            "main/typings/some-owner/some-name/v4//action-types.yml",
//                    ),
//                    -> storedInTypingCatalogForOlderVersion
//                    else -> throw IOException()
//                }
//            }
//            val actionCoord = ActionCoords("some-owner", "some-name", "v6")
//
//            // When
//            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)
//
//            // Then
//            types shouldBe Pair(mapOf("stored-in-typing-catalog-for-older-version" to StringTyping), TypingActualSource.TYPING_CATALOG)
//        }
//
//        test("metadata available but no version available") {
//            // Given
//            val fetchUri: (URI) -> String = {
//                when (it) {
//                    URI(
//                        "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
//                            "main/typings/some-owner/some-name/metadata.yml",
//                    ),
//                    -> metadata
//                    else -> throw IOException()
//                }
//            }
//            val actionCoord = ActionCoords("some-owner", "some-name", "v1")
//
//            // When
//            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)
//
//            // Then
//            types shouldBe Pair(emptyMap(), null)
//        }
//
//        test("no typings at all") {
//            // Given
//            val fetchUri: (URI) -> String = { throw IOException() }
//            val actionCoord = ActionCoords("some-owner", "some-name", "v3")
//
//            // When
//            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), fetchUri = fetchUri)
//
//            // Then
//            types shouldBe Pair(emptyMap(), null)
//        }
//    }
})
