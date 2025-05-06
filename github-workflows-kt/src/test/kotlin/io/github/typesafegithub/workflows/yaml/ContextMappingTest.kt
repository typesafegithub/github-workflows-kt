package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.contexts.Contexts
import io.github.typesafegithub.workflows.domain.contexts.GithubContext
import io.github.typesafegithub.workflows.domain.contexts.GithubContextEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ContextMappingTest :
    FunSpec({
        test("successfully load all supported contexts with all supported fields") {
            // Given
            val testGithubContextJson = javaClass.getResource("/contexts/github-all-fields.json")!!.readText()

            // When
            val contexts =
                loadContextsFromEnvVars(
                    getenv = mapOf(
                        "GHWKT_GITHUB_CONTEXT_JSON" to testGithubContextJson,
                    )::get,
                )

            // Then
            contexts shouldBe
                Contexts(
                    github =
                        GithubContext(
                            repository = "some-owner/some-repo",
                            sha = "db76dd0f1149901e1cdf60ec98d568b32fa7eb71",
                            ref = "refs/heads/main",
                            event =
                                GithubContextEvent(
                                    after = "1383af4847629428f1675f5c2e81e67cc3a4efb0",
                                ),
                            event_name = "push",
                        ),
                )
        }

        test("successfully load all supported contexts with only required fields") {
            // Given
            val testGithubContextJson = javaClass.getResource("/contexts/github-required-fields.json")!!.readText()

            // When
            val contexts =
                loadContextsFromEnvVars(
                    getenv = mapOf(
                        "GHWKT_GITHUB_CONTEXT_JSON" to testGithubContextJson,
                    )::get,
                )

            // Then
            contexts shouldBe
                Contexts(
                    github =
                        GithubContext(
                            repository = "some-owner/some-repo",
                            sha = "db76dd0f1149901e1cdf60ec98d568b32fa7eb71",
                            event = GithubContextEvent(),
                            event_name = "push",
                        ),
                )
        }
    })
