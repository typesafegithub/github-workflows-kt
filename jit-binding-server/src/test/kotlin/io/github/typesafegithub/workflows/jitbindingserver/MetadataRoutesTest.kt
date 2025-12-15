package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.mavenbinding.VersionArtifacts
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class MetadataRoutesTest :
    FunSpec({
        context("artifacts for a given package") {
            test("when some artifacts were generated") {
                testApplication {
                    // Given
                    application {
                        appModule(
                            buildPackageArtifacts = { _, _, _ ->
                                mapOf("maven-metadata.xml" to "Some XML contents")
                            },
                            getGithubAuthToken = { "some-token" },
                            // Irrelevant for these tests.
                            buildVersionArtifacts = { _, _ ->
                                VersionArtifacts(
                                    files = emptyMap(),
                                    typingActualSource = null,
                                )
                            },
                        )
                    }

                    // When
                    val response = client.get("some-owner/some-action/maven-metadata.xml")

                    // Then
                    response.status shouldBe HttpStatusCode.OK
                    response.bodyAsText() shouldBe "Some XML contents"
                }
            }

            test("when no artifacts could be generated") {
                testApplication {
                    // Given
                    application {
                        appModule(
                            buildPackageArtifacts = { _, _, _ ->
                                emptyMap()
                            },
                            getGithubAuthToken = { "some-token" },
                            // Irrelevant for these tests.
                            buildVersionArtifacts = { _, _ ->
                                VersionArtifacts(
                                    files = emptyMap(),
                                    typingActualSource = null,
                                )
                            },
                        )
                    }

                    // When
                    val response = client.get("some-owner/some-action/maven-metadata.xml")

                    // Then
                    response.status shouldBe HttpStatusCode.NotFound
                }
            }

            test("when artifact generation fails") {
                testApplication {
                    // Given
                    application {
                        appModule(
                            buildPackageArtifacts = { _, _, _ ->
                                error("An internal error occurred!")
                            },
                            getGithubAuthToken = { "some-token" },
                            // Irrelevant for these tests.
                            buildVersionArtifacts = { _, _ ->
                                VersionArtifacts(
                                    files = emptyMap(),
                                    typingActualSource = null,
                                )
                            },
                        )
                    }

                    // When
                    val response = client.get("some-owner/some-action/maven-metadata.xml")

                    // Then
                    response.status shouldBe HttpStatusCode.InternalServerError
                }
            }

            test("when binding generation fails and then succeeds, and two requests are made") {
                testApplication {
                    // Given
                    val mockBuildPackageArtifacts =
                        mockk<
                            (
                                ActionCoords,
                                String,
                                (Collection<ActionCoords>) -> Unit,
                            ) -> Map<String, String>,
                        >()
                    every { mockBuildPackageArtifacts(any(), any(), any()) } throws
                        Exception("An internal error occurred!") andThen
                        mapOf("maven-metadata.xml" to "Some XML contents")
                    application {
                        appModule(
                            buildPackageArtifacts = mockBuildPackageArtifacts,
                            getGithubAuthToken = { "some-token" },
                            // Irrelevant for these tests.
                            buildVersionArtifacts = { _, _ ->
                                VersionArtifacts(
                                    files = emptyMap(),
                                    typingActualSource = null,
                                )
                            },
                        )
                    }

                    // When
                    val response = client.get("some-owner/some-action/maven-metadata.xml")
                    // Then
                    response.status shouldBe HttpStatusCode.InternalServerError

                    // When
                    val response2 = client.get("some-owner/some-action/maven-metadata.xml")
                    // Then
                    response2.status shouldBe HttpStatusCode.OK

                    verify(exactly = 2) { mockBuildPackageArtifacts(any(), any(), any()) }
                }
            }
        }
    })
