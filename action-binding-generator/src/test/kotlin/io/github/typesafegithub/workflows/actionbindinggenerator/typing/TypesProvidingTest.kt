package io.github.typesafegithub.workflows.actionbindinggenerator.typing

import com.charleskorn.kaml.ForbiddenAnchorOrAliasException
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionTypings
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.mockClientReturning
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode

class TypesProvidingTest :
    FunSpec({
        test("copes with blank typing") {
            // Given
            val actionTypesYml = " "
            val actionCoord = ActionCoords("some-owner", "some-name", "v1")

            // When
            val types =
                actionCoord.provideTypes(
                    metadataRevision = CommitHash("some-hash"),
                    mockClientReturning(actionTypesYml),
                )

            // Then
            types.inputTypings shouldBe emptyMap()
        }

        test("copes with comment-only typing") {
            // Given
            val actionTypesYml = "#"
            val actionCoord = ActionCoords("some-owner", "some-name", "v1")

            // When
            val types =
                actionCoord.provideTypes(
                    metadataRevision = CommitHash("some-hash"),
                    mockClientReturning(actionTypesYml),
                )

            // Then
            types.inputTypings shouldBe emptyMap()
        }

        test("copes with empty inputs") {
            // Given
            val actionTypesYml = "inputs:"
            val actionCoord = ActionCoords("some-owner", "some-name", "v1")

            // When
            val types =
                actionCoord.provideTypes(
                    metadataRevision = CommitHash("some-hash"),
                    mockClientReturning(actionTypesYml),
                )

            // Then
            types.inputTypings shouldBe emptyMap()
        }

        test("copes with empty outputs") {
            // Given
            val actionTypesYml = "outputs:"
            val actionCoord = ActionCoords("some-owner", "some-name", "v1")

            // When
            val types =
                actionCoord.provideTypes(
                    metadataRevision = CommitHash("some-hash"),
                    mockClientReturning(actionTypesYml),
                )

            // Then
            types.inputTypings shouldBe emptyMap()
        }

        test("copes with empty inputs and outputs") {
            // Given
            val actionTypesYml =
                """
                inputs:
                outputs:
                """.trimIndent()
            val actionCoord = ActionCoords("some-owner", "some-name", "v1")

            // When
            val types =
                actionCoord.provideTypes(
                    metadataRevision = CommitHash("some-hash"),
                    mockClientReturning(actionTypesYml),
                )

            // Then
            types.inputTypings shouldBe emptyMap()
        }

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
            val types =
                actionCoord.provideTypes(
                    metadataRevision = CommitHash("some-hash"),
                    mockClientReturning(actionTypesYml),
                )

            // Then
            types.inputTypings shouldBe
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
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yml"
                            ) {
                                respond(hostedByActionYml)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("hosted-by-action-yml" to StringTyping),
                        source = TypingActualSource.ACTION,
                    )
            }

            test("only hosted by the subaction (.yml)") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/some-owner/some-name/some-hash/some-sub/action-types.yml"
                            ) {
                                respond(hostedByActionYml)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("hosted-by-action-yml" to StringTyping),
                        source = TypingActualSource.ACTION,
                    )
            }

            test("only hosted by the action (.yaml)") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yaml"
                            ) {
                                respond(hostedByActionYaml)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("hosted-by-action-yaml" to StringTyping),
                        source = TypingActualSource.ACTION,
                    )
            }

            test("only hosted by the subaction (.yaml)") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/some-owner/some-name/some-hash/some-sub/action-types.yaml"
                            ) {
                                respond(hostedByActionYaml)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("hosted-by-action-yaml" to StringTyping),
                        source = TypingActualSource.ACTION,
                    )
            }

            test("only hosted by the action, both extensions") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yml"
                            ) {
                                respond(hostedByActionYml)
                            } else if (request.url.toString() ==
                                "https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yaml"
                            ) {
                                respond(hostedByActionYaml)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("hosted-by-action-yml" to StringTyping),
                        source = TypingActualSource.ACTION,
                    )
            }

            test("only hosted by the subaction, both extensions") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/some-owner/some-name/some-hash/some-sub/action-types.yml"
                            ) {
                                respond(hostedByActionYml)
                            } else if (request.url.toString() ==
                                "https://raw.githubusercontent.com/some-owner/some-name/some-hash/some-sub/action-types.yaml"
                            ) {
                                respond(hostedByActionYaml)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("hosted-by-action-yml" to StringTyping),
                        source = TypingActualSource.ACTION,
                    )
            }

            test("only stored in typing catalog") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/action-types.yml"
                            ) {
                                respond(storedInTypingCatalog)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("stored-in-typing-catalog" to StringTyping),
                        source = TypingActualSource.TYPING_CATALOG,
                    )
            }

            test("only stored in typing catalog, under lowercase name and owner") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/action-types.yml"
                            ) {
                                respond(storedInTypingCatalog)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("Some-owner", "Some-name", "v3")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("stored-in-typing-catalog" to StringTyping),
                        source = TypingActualSource.TYPING_CATALOG,
                    )
            }

            test("only stored in typing catalog for subaction") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/some-sub/action-types.yml"
                            ) {
                                respond(storedInTypingCatalog)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("stored-in-typing-catalog" to StringTyping),
                        source = TypingActualSource.TYPING_CATALOG,
                    )
            }

            test("hosted by action and stored in typing catalog") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() == "https://raw.githubusercontent.com/some-owner/some-name/" +
                                "some-hash/action-types.yml"
                            ) {
                                respond(hostedByActionYml)
                            } else if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/action-types.yml"
                            ) {
                                respond(storedInTypingCatalog)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("hosted-by-action-yml" to StringTyping),
                        source = TypingActualSource.ACTION,
                    )
            }

            test("hosted by subaction and stored in typing catalog") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() == "https://raw.githubusercontent.com/some-owner/some-name/" +
                                "some-hash/some-sub/action-types.yml"
                            ) {
                                respond(hostedByActionYml)
                            } else if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/some-sub/action-types.yml"
                            ) {
                                respond(storedInTypingCatalog)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3", FULL, "some-sub")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("hosted-by-action-yml" to StringTyping),
                        source = TypingActualSource.ACTION,
                    )
            }

            test("only stored in typing catalog for older version") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/metadata.yml"
                            ) {
                                respond(metadata)
                            } else if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v4/action-types.yml"
                            ) {
                                respond(storedInTypingCatalogForOlderVersion)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("Some-owner", "Some-name", "v6")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("stored-in-typing-catalog-for-older-version" to StringTyping),
                        source = TypingActualSource.TYPING_CATALOG,
                        fromFallbackVersion = true,
                    )
            }

            test("only stored in typing catalog for older version, under lowercase name and owner") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/metadata.yml"
                            ) {
                                respond(metadata)
                            } else if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v4/action-types.yml"
                            ) {
                                respond(storedInTypingCatalogForOlderVersion)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v6")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("stored-in-typing-catalog-for-older-version" to StringTyping),
                        source = TypingActualSource.TYPING_CATALOG,
                        fromFallbackVersion = true,
                    )
            }

            test("only stored in typing catalog for older version of subaction") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/metadata.yml"
                            ) {
                                respond(metadata)
                            } else if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v4/some-sub/action-types.yml"
                            ) {
                                respond(storedInTypingCatalogForOlderVersion)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v6", FULL, "some-sub")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings = mapOf("stored-in-typing-catalog-for-older-version" to StringTyping),
                        source = TypingActualSource.TYPING_CATALOG,
                        fromFallbackVersion = true,
                    )
            }

            test("metadata available but no version available") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/metadata.yml"
                            ) {
                                respond(metadata)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v1")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe ActionTypings(inputTypings = emptyMap(), source = null)
            }

            test("no typings at all") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine {
                            respond("Not found", status = HttpStatusCode.NotFound)
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe ActionTypings(inputTypings = emptyMap(), source = null)
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
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/some-owner/some-name/some-hash/action-types.yml"
                            ) {
                                respond(typingYml)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings =
                            mapOf(
                                "granted-scopes" to
                                    ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                                "granted-scopes2" to
                                    ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                                "granted-scopes3" to
                                    ListOfTypings("""\n""", EnumTyping("GrantedScopes", listOf("read", "write"))),
                            ),
                        source = TypingActualSource.ACTION,
                    )
            }

            test("hosted by typing catalog") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v3/action-types.yml"
                            ) {
                                respond(typingYml)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings =
                            mapOf(
                                "granted-scopes" to
                                    ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                                "granted-scopes2" to
                                    ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                                "granted-scopes3" to
                                    ListOfTypings("""\n""", EnumTyping("GrantedScopes", listOf("read", "write"))),
                            ),
                        source = TypingActualSource.TYPING_CATALOG,
                    )
            }

            test("only stored in typing catalog for older version") {
                // Given
                val mockClient =
                    HttpClient(
                        MockEngine { request ->
                            if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/metadata.yml"
                            ) {
                                respond(metadata)
                            } else if (request.url.toString() ==
                                "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                                "main/typings/some-owner/some-name/v4/action-types.yml"
                            ) {
                                respond(typingYml)
                            } else {
                                respond("Not found", status = HttpStatusCode.NotFound)
                            }
                        },
                    )
                val actionCoord = ActionCoords("some-owner", "some-name", "v6")

                // When
                val types =
                    actionCoord.provideTypes(
                        metadataRevision = CommitHash("some-hash"),
                        httpClient = mockClient,
                    )

                // Then
                types shouldBe
                    ActionTypings(
                        inputTypings =
                            mapOf(
                                "granted-scopes" to
                                    ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                                "granted-scopes2" to
                                    ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
                                "granted-scopes3" to
                                    ListOfTypings("""\n""", EnumTyping("GrantedScopes", listOf("read", "write"))),
                            ),
                        source = TypingActualSource.TYPING_CATALOG,
                        fromFallbackVersion = true,
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
                val actionCoord = ActionCoords("some-owner", "some-name", "v3")

                // Expect
                val exception =
                    shouldThrow<ForbiddenAnchorOrAliasException> {
                        actionCoord.provideTypes(
                            metadataRevision = CommitHash("some-hash"),
                            httpClient = mockClientReturning(billionLaughsAttack),
                        )
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
            val mockClient =
                HttpClient(
                    MockEngine { request ->
                        if (request.url.toString() ==
                            "https://raw.githubusercontent.com/typesafegithub/github-actions-typing-catalog/" +
                            "main/typings/some-owner/some-name/metadata.yml"
                        ) {
                            respond(metadata)
                        } else {
                            respond("Not found", status = HttpStatusCode.NotFound)
                        }
                    },
                )
            val actionCoord = ActionCoords("some-owner", "some-name", "v6-beta")

            // When
            val types = actionCoord.provideTypes(metadataRevision = CommitHash("some-hash"), httpClient = mockClient)

            // Then
            types shouldBe ActionTypings(inputTypings = emptyMap(), source = null)
        }
    })
