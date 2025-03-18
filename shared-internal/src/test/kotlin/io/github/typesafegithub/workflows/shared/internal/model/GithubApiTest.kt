package io.github.typesafegithub.workflows.shared.internal.model

import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.JsonConvertException
import io.ktor.utils.io.ByteReadChannel

class GithubApiTest :
    FunSpec({
        test("branches with major versions and tags with other versions") {
            // Given
            val mockEngine =
                MockEngine { request ->
                    if ("matching-refs/tags" in request.url.fullPath) {
                        respond(
                            // language=json
                            content =
                                ByteReadChannel(
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
                                    """.trimIndent(),
                                ),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json"),
                        )
                    } else if ("matching-refs/heads" in request.url.fullPath) {
                        respond(
                            // language=json
                            content =
                                ByteReadChannel(
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
                                    """.trimIndent(),
                                ),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json"),
                        )
                    } else {
                        respond(
                            content = ByteReadChannel("The mock client wasn't prepared for this request"),
                            status = HttpStatusCode.NotFound,
                        )
                    }
                }

            // When
            val versions =
                fetchAvailableVersions(
                    owner = "some-owner",
                    name = "some-name",
                    githubToken = "token",
                    httpClientEngine = mockEngine,
                )

            // Then
            versions shouldBe
                listOf(
                    Version("v1.0.0"),
                    Version("v1.0.1"),
                    Version("v1"),
                    Version("v2"),
                )
        }

        test("error occurs when fetching branches and tags") {
            // Given
            val mockEngine =
                MockEngine { request ->
                    respond(
                        // language=json
                        content = ByteReadChannel("""{"message":  "There was a problem!"}"""),
                        status = HttpStatusCode.Forbidden,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }

            // Then
            // TODO: fix - right now, the logic fails if it gets something unparseable.
            //  The test just shows the current behavior, not the intended behavior.
            //  To be fixed in https://github.com/typesafegithub/github-workflows-kt/issues/1855
            shouldThrow<JsonConvertException> {
                // When
                fetchAvailableVersions(
                    owner = "some-owner",
                    name = "some-name",
                    githubToken = "token",
                    httpClientEngine = mockEngine,
                )
            }.also {
                it.message shouldContain "Unexpected JSON token"
            }
        }
    })
