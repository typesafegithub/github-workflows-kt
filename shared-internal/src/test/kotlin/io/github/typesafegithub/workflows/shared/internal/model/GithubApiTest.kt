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
                val repositoryResponse =
                    """
                    {
                      "id": 429460367,
                      "node_id": "R_kgDOGZkLjw",
                      "name": "some-name",
                      "full_name": "some-owner/some-name",
                      "private": false,
                      "owner": {
                        "login": "some-owner",
                        "id": 1577251,
                        "node_id": "MDQ6VXNlcjE1NzcyNTE=",
                        "avatar_url": "https://avatars.githubusercontent.com/u/1577251?v=4",
                        "gravatar_id": "",
                        "url": "https://api.github.com/users/LeoColman",
                        "html_url": "https://github.com/LeoColman",
                        "followers_url": "https://api.github.com/users/LeoColman/followers",
                        "following_url": "https://api.github.com/users/LeoColman/following{/other_user}",
                        "gists_url": "https://api.github.com/users/LeoColman/gists{/gist_id}",
                        "starred_url": "https://api.github.com/users/LeoColman/starred{/owner}{/repo}",
                        "subscriptions_url": "https://api.github.com/users/LeoColman/subscriptions",
                        "organizations_url": "https://api.github.com/users/LeoColman/orgs",
                        "repos_url": "https://api.github.com/users/LeoColman/repos",
                        "events_url": "https://api.github.com/users/LeoColman/events{/privacy}",
                        "received_events_url": "https://api.github.com/users/LeoColman/received_events",
                        "type": "User",
                        "user_view_type": "public",
                        "site_admin": false
                      },
                      "html_url": "https://github.com/LeoColman/MyStack",
                      "description": null,
                      "fork": false,
                      "url": "https://api.github.com/repos/LeoColman/MyStack",
                      "forks_url": "https://api.github.com/repos/LeoColman/MyStack/forks",
                      "keys_url": "https://api.github.com/repos/LeoColman/MyStack/keys{/key_id}",
                      "collaborators_url": "https://api.github.com/repos/LeoColman/MyStack/collaborators{/collaborator}",
                      "teams_url": "https://api.github.com/repos/LeoColman/MyStack/teams",
                      "hooks_url": "https://api.github.com/repos/LeoColman/MyStack/hooks",
                      "issue_events_url": "https://api.github.com/repos/LeoColman/MyStack/issues/events{/number}",
                      "events_url": "https://api.github.com/repos/LeoColman/MyStack/events",
                      "assignees_url": "https://api.github.com/repos/LeoColman/MyStack/assignees{/user}",
                      "branches_url": "https://api.github.com/repos/LeoColman/MyStack/branches{/branch}",
                      "tags_url": "https://api.github.com/repos/LeoColman/MyStack/tags",
                      "blobs_url": "https://api.github.com/repos/LeoColman/MyStack/git/blobs{/sha}",
                      "git_tags_url": "https://api.github.com/repos/LeoColman/MyStack/git/tags{/sha}",
                      "git_refs_url": "https://api.github.com/repos/LeoColman/MyStack/git/refs{/sha}",
                      "trees_url": "https://api.github.com/repos/LeoColman/MyStack/git/trees{/sha}",
                      "statuses_url": "https://api.github.com/repos/LeoColman/MyStack/statuses/{sha}",
                      "languages_url": "https://api.github.com/repos/LeoColman/MyStack/languages",
                      "stargazers_url": "https://api.github.com/repos/LeoColman/MyStack/stargazers",
                      "contributors_url": "https://api.github.com/repos/LeoColman/MyStack/contributors",
                      "subscribers_url": "https://api.github.com/repos/LeoColman/MyStack/subscribers",
                      "subscription_url": "https://api.github.com/repos/LeoColman/MyStack/subscription",
                      "commits_url": "https://api.github.com/repos/LeoColman/MyStack/commits{/sha}",
                      "git_commits_url": "https://api.github.com/repos/LeoColman/MyStack/git/commits{/sha}",
                      "comments_url": "https://api.github.com/repos/LeoColman/MyStack/comments{/number}",
                      "issue_comment_url": "https://api.github.com/repos/LeoColman/MyStack/issues/comments{/number}",
                      "contents_url": "https://api.github.com/repos/LeoColman/MyStack/contents/{+path}",
                      "compare_url": "https://api.github.com/repos/LeoColman/MyStack/compare/{base}...{head}",
                      "merges_url": "https://api.github.com/repos/LeoColman/MyStack/merges",
                      "archive_url": "https://api.github.com/repos/LeoColman/MyStack/{archive_format}{/ref}",
                      "downloads_url": "https://api.github.com/repos/LeoColman/MyStack/downloads",
                      "issues_url": "https://api.github.com/repos/LeoColman/MyStack/issues{/number}",
                      "pulls_url": "https://api.github.com/repos/LeoColman/MyStack/pulls{/number}",
                      "milestones_url": "https://api.github.com/repos/LeoColman/MyStack/milestones{/number}",
                      "notifications_url": "https://api.github.com/repos/LeoColman/MyStack/notifications{?since,all,participating}",
                      "labels_url": "https://api.github.com/repos/LeoColman/MyStack/labels{/name}",
                      "releases_url": "https://api.github.com/repos/LeoColman/MyStack/releases{/id}",
                      "deployments_url": "https://api.github.com/repos/LeoColman/MyStack/deployments",
                      "created_at": "2021-11-18T14:26:50Z",
                      "updated_at": "2025-04-23T19:38:18Z",
                      "pushed_at": "2025-04-23T19:38:15Z",
                      "git_url": "git://github.com/LeoColman/MyStack.git",
                      "ssh_url": "git@github.com:LeoColman/MyStack.git",
                      "clone_url": "https://github.com/LeoColman/MyStack.git",
                      "svn_url": "https://github.com/LeoColman/MyStack",
                      "homepage": null,
                      "size": 24074,
                      "stargazers_count": 1,
                      "watchers_count": 1,
                      "language": null,
                      "has_issues": true,
                      "has_projects": false,
                      "has_downloads": true,
                      "has_wiki": false,
                      "has_pages": false,
                      "has_discussions": false,
                      "forks_count": 1,
                      "mirror_url": null,
                      "archived": false,
                      "disabled": false,
                      "open_issues_count": 1,
                      "license": {
                        "key": "mit",
                        "name": "MIT License",
                        "spdx_id": "MIT",
                        "url": "https://api.github.com/licenses/mit",
                        "node_id": "MDc6TGljZW5zZTEz"
                      },
                      "allow_forking": true,
                      "is_template": false,
                      "web_commit_signoff_required": false,
                      "topics": [],
                      "visibility": "public",
                      "forks": 1,
                      "open_issues": 1,
                      "watchers": 1,
                      "default_branch": "main",
                      "temp_clone_token": null,
                      "network_count": 1,
                      "subscribers_count": 1
                    }
                    """.trimIndent()
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
                    .`when`(request().withPath("/repos/$owner/$name"))
                    .respond(
                        response()
                            .withStatusCode(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(repositoryResponse),
                    )
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
