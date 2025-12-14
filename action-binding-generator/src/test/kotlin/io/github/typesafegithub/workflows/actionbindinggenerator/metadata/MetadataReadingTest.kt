package io.github.typesafegithub.workflows.actionbindinggenerator.metadata

import com.charleskorn.kaml.MissingRequiredPropertyException
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.io.IOException
import java.net.URI

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
            var requestedUris = mutableListOf<URI>()

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    fetchUri = {
                        requestedUris.add(it)

                        if (it.toURL().toString().endsWith(".yml")) {
                            metadataYaml
                        } else {
                            throw IOException("Failed to fetch the file")
                        }
                    },
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    URI("https://raw.githubusercontent.com/test-owner/test-name/commit-hash/action.yml"),
                )
        }

        test("success, commit hash, .yaml exists") {
            // Given
            var requestedUris = mutableListOf<URI>()

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    fetchUri = {
                        requestedUris.add(it)

                        if (it.toURL().toString().endsWith(".yaml")) {
                            metadataYaml
                        } else {
                            throw IOException("Failed to fetch the file")
                        }
                    },
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    URI("https://raw.githubusercontent.com/test-owner/test-name/commit-hash/action.yml"),
                    URI("https://raw.githubusercontent.com/test-owner/test-name/commit-hash/action.yaml"),
                )
        }

        test("success, commit hash, both .yml and .yaml exist") {
            // Given
            var requestedUris = mutableListOf<URI>()

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    fetchUri = {
                        requestedUris.add(it)
                        metadataYaml
                    },
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    URI("https://raw.githubusercontent.com/test-owner/test-name/commit-hash/action.yml"),
                )
        }

        test("success, newest for version, .yml exists") {
            // Given
            var requestedUris = mutableListOf<URI>()

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = NewestForVersion,
                    fetchUri = {
                        requestedUris.add(it)

                        if (it.toURL().toString().endsWith(".yml")) {
                            metadataYaml
                        } else {
                            throw IOException("Failed to fetch the file")
                        }
                    },
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    URI("https://raw.githubusercontent.com/test-owner/test-name/v1/action.yml"),
                )
        }

        test("success, newest for version, .yaml exists") {
            // Given
            var requestedUris = mutableListOf<URI>()

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = NewestForVersion,
                    fetchUri = {
                        requestedUris.add(it)

                        if (it.toURL().toString().endsWith(".yaml")) {
                            metadataYaml
                        } else {
                            throw IOException("Failed to fetch the file")
                        }
                    },
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    URI("https://raw.githubusercontent.com/test-owner/test-name/v1/action.yml"),
                    URI("https://raw.githubusercontent.com/test-owner/test-name/v1/action.yaml"),
                )
        }

        test("success, newest for version, both .yml and .yaml exist") {
            // Given
            var requestedUris = mutableListOf<URI>()

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = NewestForVersion,
                    fetchUri = {
                        requestedUris.add(it)
                        metadataYaml
                    },
                )

            // Then
            metadata shouldBe expectedMetadata
            requestedUris shouldBe
                listOf(
                    URI("https://raw.githubusercontent.com/test-owner/test-name/v1/action.yml"),
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

            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    fetchUri = { metadataYaml },
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

            // Then
            shouldThrow<MissingRequiredPropertyException> {
                // When
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    fetchUri = { metadataYaml },
                )
            }
        }

        test("all requests fail") {
            // When
            val metadata =
                coords.fetchMetadata(
                    metadataRevision = CommitHash("commit-hash"),
                    fetchUri = { throw IOException("Failed to fetch the file") },
                )

            // Then
            metadata.shouldBeNull()
        }
    })
