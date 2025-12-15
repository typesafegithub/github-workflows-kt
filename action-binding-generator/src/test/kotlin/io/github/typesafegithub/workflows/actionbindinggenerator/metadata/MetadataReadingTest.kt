package io.github.typesafegithub.workflows.actionbindinggenerator.metadata

import com.charleskorn.kaml.MissingRequiredPropertyException
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException

class MetadataReadingTest :
    FunSpec({
        val coords = ActionCoords(owner = "test-owner", name = "test-name", version = "v1")
        val metadataYaml =
            """
            name: Some cool action
            description: Really cool action!
            inputs:
              input-1:
                description: Some input
                default: false
                required: true
                deprecationMessage: This input has been deprecated
            outputs:
              output-1:
                description: Some output
            """.trimIndent()
        val expectedMetadata =
            Metadata(
                name = "Some cool action",
                description = "Really cool action!",
                inputs =
                    mapOf(
                        "input-1" to
                            Input(
                                description = "Some input",
                                default = "false",
                                required = true,
                                deprecationMessage = "This input has been deprecated",
                            ),
                    ),
                outputs =
                    mapOf(
                        "output-1" to
                            Output(
                                description = "Some output",
                            ),
                    ),
            )

        test("success, commit hash, .yml exists") {
            // Given
            var requestedUris = mutableListOf<String>()
            val mockClient =
                HttpClient(
                    MockEngine { request ->
                        requestedUris.add(request.url.toString())
                        if (request.url.toString().endsWith(".yml")) {
                            respond(metadataYaml)
                        } else {
                            respond("Not found", status = HttpStatusCode.NotFound)
                        }
                    },
                )

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    httpClient = mockClient,
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    "https://raw.githubusercontent.com/test-owner/test-name/commit-hash/action.yml",
                )
        }

        test("success, commit hash, .yaml exists") {
            // Given
            var requestedUris = mutableListOf<String>()
            val mockClient =
                HttpClient(
                    MockEngine { request ->
                        requestedUris.add(request.url.toString())
                        if (request.url.toString().endsWith(".yaml")) {
                            respond(metadataYaml)
                        } else {
                            respond("Not found", status = HttpStatusCode.NotFound)
                        }
                    },
                )

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    httpClient = mockClient,
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    "https://raw.githubusercontent.com/test-owner/test-name/commit-hash/action.yml",
                    "https://raw.githubusercontent.com/test-owner/test-name/commit-hash/action.yaml",
                )
        }

        test("success, commit hash, both .yml and .yaml exist") {
            // Given
            var requestedUris = mutableListOf<String>()
            val mockClient =
                HttpClient(
                    MockEngine { request ->
                        requestedUris.add(request.url.toString())
                        respond(metadataYaml)
                    },
                )

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    httpClient = mockClient,
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    "https://raw.githubusercontent.com/test-owner/test-name/commit-hash/action.yml",
                )
        }

        test("success, newest for version, .yml exists") {
            // Given
            var requestedUris = mutableListOf<String>()
            val mockClient =
                HttpClient(
                    MockEngine { request ->
                        requestedUris.add(request.url.toString())
                        if (request.url.toString().endsWith(".yml")) {
                            respond(metadataYaml)
                        } else {
                            respond("Not found", status = HttpStatusCode.NotFound)
                        }
                    },
                )

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = NewestForVersion,
                    httpClient = mockClient,
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    "https://raw.githubusercontent.com/test-owner/test-name/v1/action.yml",
                )
        }

        test("success, newest for version, .yaml exists") {
            // Given
            var requestedUris = mutableListOf<String>()
            val mockClient =
                HttpClient(
                    MockEngine { request ->
                        requestedUris.add(request.url.toString())
                        if (request.url.toString().endsWith(".yaml")) {
                            respond(metadataYaml)
                        } else {
                            respond("Not found", status = HttpStatusCode.NotFound)
                        }
                    },
                )

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = NewestForVersion,
                    httpClient = mockClient,
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    "https://raw.githubusercontent.com/test-owner/test-name/v1/action.yml",
                    "https://raw.githubusercontent.com/test-owner/test-name/v1/action.yaml",
                )
        }

        test("success, newest for version, both .yml and .yaml exist") {
            // Given
            var requestedUris = mutableListOf<String>()
            val mockClient =
                HttpClient(
                    MockEngine { request ->
                        requestedUris.add(request.url.toString())
                        respond(metadataYaml)
                    },
                )

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = NewestForVersion,
                    httpClient = mockClient,
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    "https://raw.githubusercontent.com/test-owner/test-name/v1/action.yml",
                )
        }

        test("unknown field") {
            // Given
            val metadataYaml =
                """
                name: Some cool action
                description: Really cool action!
                unknown: field
                inputs:
                  input-1:
                    description: Some input
                    default: false
                    required: true
                    deprecationMessage: This input has been deprecated
                outputs:
                  output-1:
                    description: Some output
                """.trimIndent()
            val mockClient =
                HttpClient(
                    MockEngine {
                        respond(metadataYaml)
                    },
                )

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    httpClient = mockClient,
                )

            // Then
            metadata shouldBe expectedMetadata
        }

        test("missing required field") {
            // Given
            val metadataYaml =
                """
                description: Really cool action!
                unknown: field
                inputs:
                  input-1:
                    description: Some input
                    default: false
                    required: true
                    deprecationMessage: This input has been deprecated
                outputs:
                  output-1:
                    description: Some output
                """.trimIndent()
            val mockClient =
                HttpClient(
                    MockEngine {
                        respond(metadataYaml)
                    },
                )

            // Then
            shouldThrow<MissingRequiredPropertyException> {
                // When
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    httpClient = mockClient,
                )
            }
        }

        test("no resources exist") {
            // Given
            val mockClient =
                HttpClient(
                    MockEngine {
                        respond("Not found", status = HttpStatusCode.NotFound)
                    },
                )

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    httpClient = mockClient,
                )

            // Then
            metadata.shouldBeNull()
        }

        test("all requests fail") {
            // Given
            val mockClient =
                HttpClient(
                    MockEngine {
                        respond("Internal error", status = HttpStatusCode.InternalServerError)
                    },
                )

            // Then
            shouldThrow<IOException> {
                // When
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    httpClient = mockClient,
                )
            }
        }
    })
