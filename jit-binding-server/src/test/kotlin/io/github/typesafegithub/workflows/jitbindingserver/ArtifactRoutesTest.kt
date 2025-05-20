package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.mavenbinding.TextArtifact
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

class ArtifactRoutesTest :
    FunSpec({
        context("artifacts for a given version") {
            test("when some artifacts were generated") {
                testApplication {
                    // Given
                    application {
                        appModule(
                            buildVersionArtifacts = {
                                VersionArtifacts(
                                    files = mapOf("some-action-v4.pom" to TextArtifact { "Some POM contents" }),
                                    typingActualSource = TypingActualSource.TYPING_CATALOG,
                                )
                            },
                            // Irrelevant for these tests.
                            buildPackageArtifacts = { _, _, _, _ -> emptyMap() },
                            getGithubAuthToken = { "" },
                        )
                    }

                    // When
                    val response = client.get("some-owner/some-action/v4/some-action-v4.pom")

                    // Then
                    response.status shouldBe HttpStatusCode.OK
                    response.bodyAsText() shouldBe "Some POM contents"
                }
            }

            test("when no artifacts could be generated") {
                testApplication {
                    // Given
                    application {
                        appModule(
                            buildVersionArtifacts = { null },
                            // Irrelevant for these tests.
                            buildPackageArtifacts = { _, _, _, _ -> emptyMap() },
                            getGithubAuthToken = { "" },
                        )
                    }

                    // When
                    val response = client.get("some-owner/some-action/v4/some-action-v4.pom")

                    // Then
                    response.status shouldBe HttpStatusCode.NotFound
                }
            }

            test("when binding generation fails") {
                testApplication {
                    // Given
                    application {
                        appModule(
                            buildVersionArtifacts = { error("An internal error occurred!") },
                            // Irrelevant for these tests.
                            buildPackageArtifacts = { _, _, _, _ -> emptyMap() },
                            getGithubAuthToken = { "" },
                        )
                    }

                    // When
                    val response = client.get("some-owner/some-action/v4/some-action-v4.pom")

                    // Then
                    response.status shouldBe HttpStatusCode.InternalServerError
                }
            }

            test("when binding generation fails and then succeeds, and two requests are made") {
                testApplication {
                    // Given
                    val mockBuildVersionArtifacts = mockk<(CacheKey) -> VersionArtifacts?>()
                    every { mockBuildVersionArtifacts(any()) } throws
                        Exception("An internal error occurred!") andThen
                        VersionArtifacts(
                            files = mapOf("some-action-v4.pom" to TextArtifact { "Some POM contents" }),
                            typingActualSource = TypingActualSource.TYPING_CATALOG,
                        )
                    application {
                        appModule(
                            buildVersionArtifacts = mockBuildVersionArtifacts,
                            // Irrelevant for these tests.
                            buildPackageArtifacts = { _, _, _, _ -> emptyMap() },
                            getGithubAuthToken = { "" },
                        )
                    }

                    // When
                    val response = client.get("some-owner/some-action/v4/some-action-v4.pom")
                    // Then
                    response.status shouldBe HttpStatusCode.InternalServerError

                    // When
                    val response2 = client.get("some-owner/some-action/v4/some-action-v4.pom")
                    // Then
                    response2.status shouldBe HttpStatusCode.OK

                    verify(exactly = 2) { mockBuildVersionArtifacts(any()) }
                }
            }
        }
    })
