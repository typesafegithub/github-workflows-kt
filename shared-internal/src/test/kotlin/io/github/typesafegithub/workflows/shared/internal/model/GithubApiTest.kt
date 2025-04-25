package io.github.typesafegithub.workflows.shared.internal.model

import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.mockserver.MockServerExtension
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import org.mockserver.integration.ClientAndServer
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
                mockServer.mockRepositoryResponse(owner, name)
                mockServer.mockTagsResponse(owner, name)
                mockServer.mockHeadsResponse(owner, name)

                // When
                val versionsOrError =
                    fetchAvailableVersions(
                        owner = owner,
                        name = name,
                        githubAuthToken = "token",
                        githubEndpoint = "http://localhost:${mockServer.port}",
                    )

                // Then
                versionsOrError shouldBeSuccess
                    listOf(
                        Version("v1.0.0"),
                        Version("v1.0.1"),
                        Version("v1"),
                        Version("v2"),
                    )
            }

            test("error occurs when fetching branches and tags") {
                // Given
                // No mocks setup (will fail)

                // When
                val versionOrError =
                    fetchAvailableVersions(
                        owner = owner,
                        name = name,
                        githubAuthToken = "token",
                        githubEndpoint = "http://localhost:${mockServer.port}",
                    )

                // Then
                versionOrError.shouldBeFailure()
            }
        },
    )

private fun ClientAndServer.mockHeadsResponse(
    owner: String,
    name: String,
) {
    mockResponse("/repos/$owner/$name/git/refs/heads", "heads.json")
}

private fun ClientAndServer.mockTagsResponse(
    owner: String,
    name: String,
) {
    mockResponse("/repos/$owner/$name/git/refs/tags", "tags.json")
}

private fun ClientAndServer.mockRepositoryResponse(
    owner: String,
    name: String,
) {
    mockResponse("/repos/$owner/$name", "repository.json")
}

private fun ClientAndServer.mockResponse(
    path: String,
    resource: String,
) {
    `when`(request().withPath(path))
        .respond(
            response()
                .withStatusCode(200)
                .withHeader("Content-Type", "application/json")
                .withBody(readResource(resource)),
        )
}

fun readResource(path: String) =
    GithubApiTest::class.java.classLoader
        .getResourceAsStream(path)!!
        .readBytes()
