package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.mavenbinding.TextArtifact
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
                                mapOf("some-action-v4.pom" to TextArtifact { "Some POM contents" })
                            },
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
                    val mockBuildVersionArtifacts = mockk<(ActionCoords) -> Map<String, TextArtifact>?>()
                    every { mockBuildVersionArtifacts(any()) } throws
                        Exception("An internal error occurred!") andThen
                        mapOf("some-action-v4.pom" to TextArtifact { "Some POM contents" })
                    application {
                        appModule(
                            buildVersionArtifacts = mockBuildVersionArtifacts,
                        )
                    }

                    // When
                    val response = client.get("some-owner/some-action/v4/some-action-v4.pom")
                    // Then
                    response.status shouldBe HttpStatusCode.InternalServerError

                    // When
                    val response2 = client.get("some-owner/some-action/v4/some-action-v4.pom")
                    // Then
                    // This assertion represents an undesired behavior - it should retry generating the binding
                    // and return the actual POM.
                    // TODO fix in https://github.com/typesafegithub/github-workflows-kt/issues/1938
                    response2.status shouldBe HttpStatusCode.InternalServerError

                    // This assertion represents an undesired behavior - it should call the binding generation twice,
                    // first with exception, and then when it's successful.
                    // TODO fix in https://github.com/typesafegithub/github-workflows-kt/issues/1938
                    verify(exactly = 1) { mockBuildVersionArtifacts(any()) }
                }
            }
        }
    })
