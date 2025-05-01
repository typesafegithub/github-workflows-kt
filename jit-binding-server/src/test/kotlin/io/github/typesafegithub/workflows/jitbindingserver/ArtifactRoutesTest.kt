package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.mavenbinding.TextArtifact
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication

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
        }
    })
