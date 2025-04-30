package io.github.typesafegithub.workflows.jitbindingserver

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication

class ArtifactRoutesTest :
    FunSpec({
        context("artifacts for a given version") {
            test("when binding generation fails") {
                testApplication {
                    // Given
                    application {
                        routing {
                            artifactRoutes(
                                buildBindingsCache(
                                    buildVersionArtifacts = { error("An internal error occurred!") },
                                ),
                            )
                        }
                    }

                    // When
                    val response = client.get("some-owner/some-action/v4/some-action-v4.pom")
                    // This is incorrect, and will be fixed in
                    //  https://github.com/typesafegithub/github-workflows-kt/pull/1924/
                    response.status shouldBe HttpStatusCode.NotFound
                }
            }
        }
    })
