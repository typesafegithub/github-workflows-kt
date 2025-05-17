package io.github.typesafegithub.workflows.shared.internal.model

import arrow.core.left
import arrow.core.right
import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.mockserver.MockServerExtension
import io.kotest.matchers.shouldBe
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response

class GithubApiTest :
    FunSpec(
        {
            val mockServer = install(MockServerExtension())

            beforeTest {
                mockServer.reset()
            }

            val owner = "some-owner"
            val name = "some-name"

            test("branches with major versions and tags with other versions") {
                // Given
                val tagsResponse =
                    """
                    [
                        {
                            "ref":"refs/tags/v1.0.0",
                            "node_id":"MDM6UmVmMTk3ODE0NjI5OnJlZnMvdGFncy92MQ==",
                            "url":"https://api.github.com/repos/some-owner/some-name/git/refs/tags/v1",
                            "object": {
                                "sha":"544eadc6bf3d226fd7a7a9f0dc5b5bf7ca0675b9",
                                "type":"tag",
                                "url":"https://api.github.com/repos/actions/some-name/git/tags/544eadc6bf3d226fd7a7a9f0dc5b5bf7ca0675b9"
                            }
                        },
                        {
                            "ref":"refs/tags/v1.0.1",
                            "node_id":"MDM6UmVmMTk3ODE0NjI5OnJlZnMvdGFncy92MQ==",
                            "url":"https://api.github.com/repos/some-owner/some-name/git/refs/tags/v1.0.1",
                            "object": {
                                "sha":"af513c7a016048ae468971c52ed77d9562c7c819",
                                "type":"tag",
                                "url":"https://api.github.com/repos/actions/some-name/git/tags/af513c7a016048ae468971c52ed77d9562c7c819"
                            }
                        }
                    ]
                    """.trimIndent()
                val headsResponse =
                    """
                    [
                        {
                            "ref":"refs/heads/v1",
                            "node_id":"MDM6UmVmMTk3ODE0NjI5OnJlZnMvaGVhZHMvdm1qb3NlcGgvc2lsZW50LXJldi1wYXJzZQ==",
                            "url":"https://api.github.com/repos/some-owner/some-name/git/refs/heads/v1",
                            "object": {
                                "sha":"af5130cb8882054eda385840657dcbd1e19ab8f4",
                                "type":"commit",
                                "url":"https://api.github.com/repos/some-owner/some-name/git/commits/af5130cb8882054eda385840657dcbd1e19ab8f4"
                            }
                        },
                        {
                            "ref":"refs/heads/v2",
                            "node_id":"MDM6UmVmMTk3ODE0NjI5OnJlZnMvaGVhZHMvdm1qb3NlcGgvdG9vbGtpdC13aW5kb3dzLWV4ZWM=",
                            "url":"https://api.github.com/repos/some-owner/some-name/git/refs/heads/v2",
                            "object": {
                                "sha":"c22ccee38a13e34cb01a103c324adb1db665821e",
                                "type":"commit",
                                "url":"https://api.github.com/repos/some-owner/some-name/git/commits/c22ccee38a13e34cb01a103c324adb1db665821e"
                            }
                        }
                    ]
                    """.trimIndent()
                mockServer
                    .`when`(request().withPath("/repos/$owner/$name/git/matching-refs/tags/v"))
                    .respond(
                        response()
                            .withStatusCode(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(tagsResponse),
                    )
                mockServer
                    .`when`(request().withPath("/repos/$owner/$name/git/matching-refs/heads/v"))
                    .respond(
                        response()
                            .withStatusCode(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(headsResponse),
                    )

                // When
                val versionsOrError =
                    fetchAvailableVersions(
                        owner = owner,
                        name = name,
                        githubAuthToken = "token",
                        githubEndpoint = "http://localhost:${mockServer.port}",
                    )

                // Then
                versionsOrError shouldBe
                    listOf(
                        Version("v1.0.0"),
                        Version("v1.0.1"),
                        Version("v1"),
                        Version("v2"),
                    ).right()
            }

            test("error occurs when fetching branches and tags") {
                // Given
                mockServer
                    .`when`(request())
                    .respond(
                        response()
                            .withStatusCode(403)
                            .withHeader("Content-Type", "application/json")
                            .withBody("""{"message":  "There was a problem!"}"""),
                    )

                // When
                val versionOrError =
                    fetchAvailableVersions(
                        owner = owner,
                        name = name,
                        githubAuthToken = "token",
                        githubEndpoint = "http://localhost:${mockServer.port}",
                    )

                // Then
                versionOrError shouldBe
                    (
                        "Unexpected response when fetching refs from " +
                            "http://localhost:${mockServer.port}/" +
                            "repos/some-owner/some-name/git/matching-refs/tags/v. " +
                            "Status: 403 Forbidden, response: {\"message\":  \"There was a problem!\"}"
                    ).left()
            }
        },
    )
