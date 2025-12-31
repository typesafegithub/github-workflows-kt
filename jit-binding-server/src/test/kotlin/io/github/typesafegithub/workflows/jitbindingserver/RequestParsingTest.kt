package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MAJOR
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MINOR
import io.github.typesafegithub.workflows.mavenbinding.BindingsServerRequest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder

class RequestParsingTest :
    FunSpec(
        {
            context("parseRequest") {
                test("parses owner/name with version and no path, FULL by default") {
                    val parameters = createParameters(owner = "o", name = "act", version = "v1")

                    parameters.parseRequest(extractVersion = true) shouldBe
                        BindingsServerRequest(
                            rawName = "act",
                            rawVersion = "v1",
                            actionCoords =
                                ActionCoords(
                                    owner = "o",
                                    name = "act",
                                    version = "v1",
                                    significantVersion = FULL,
                                    path = null,
                                ),
                        )
                }

                test("parses owner/name with path and significant version suffix") {
                    val parameters = createParameters(owner = "o", name = "act__p1__p2___major", version = "v9")

                    parameters.parseRequest(extractVersion = true) shouldBe
                        BindingsServerRequest(
                            rawName = "act__p1__p2___major",
                            rawVersion = "v9",
                            actionCoords =
                                ActionCoords(
                                    owner = "o",
                                    name = "act",
                                    version = "v9",
                                    significantVersion = MAJOR,
                                    path = "p1/p2",
                                ),
                        )
                }

                test("when extractVersion=false, version field is set to 'irrelevant'") {
                    val parameters = createParameters(owner = "o", name = "act___minor")

                    parameters.parseRequest(extractVersion = false) shouldBe
                        BindingsServerRequest(
                            rawName = "act___minor",
                            rawVersion = null,
                            actionCoords =
                                ActionCoords(
                                    owner = "o",
                                    name = "act",
                                    version = "irrelevant",
                                    significantVersion = MINOR,
                                    path = null,
                                ),
                        )
                }

                test("unknown significant version part falls back to FULL") {
                    val parameters = createParameters(owner = "o", name = "act___weird")

                    parameters.parseRequest(extractVersion = false) shouldBe
                        BindingsServerRequest(
                            rawName = "act___weird",
                            rawVersion = null,
                            actionCoords =
                                ActionCoords(
                                    owner = "o",
                                    name = "act",
                                    version = "irrelevant",
                                    significantVersion = FULL,
                                    path = null,
                                ),
                        )
                }

                test("handles name with underscores/hyphens and path segments") {
                    val parameters =
                        createParameters(owner = "o", name = "my_action-name__dir_one__dir-two___minor", version = "v2")

                    parameters.parseRequest(extractVersion = true) shouldBe
                        BindingsServerRequest(
                            rawName = "my_action-name__dir_one__dir-two___minor",
                            rawVersion = "v2",
                            actionCoords =
                                ActionCoords(
                                    owner = "o",
                                    name = "my_action-name",
                                    version = "v2",
                                    significantVersion = MINOR,
                                    path = "dir_one/dir-two",
                                ),
                        )
                }

                test("parses commit_lenient syntax") {
                    val parameters =
                        createParameters(
                            owner = "o",
                            name = "act___commit_lenient",
                            version = "v1.2.3__323898970401d85df44b3324a610af9a862d54b3",
                        )

                    parameters.parseRequest(extractVersion = true) shouldBe
                        BindingsServerRequest(
                            rawName = "act___commit_lenient",
                            rawVersion = "v1.2.3__323898970401d85df44b3324a610af9a862d54b3",
                            actionCoords =
                                ActionCoords(
                                    owner = "o",
                                    name = "act",
                                    version = "323898970401d85df44b3324a610af9a862d54b3",
                                    significantVersion = FULL,
                                    comment = "v1.2.3",
                                    versionForTypings = "v1.2.3",
                                    path = null,
                                ),
                        )
                }

                test("parses commit_lenient syntax and no commit hash given") {
                    val parameters =
                        createParameters(
                            owner = "o",
                            name = "act___commit_lenient",
                            version = "v1.2.3",
                        )

                    parameters.parseRequest(extractVersion = true).shouldBeNull()
                }
            }
        },
    )

private fun createParameters(
    owner: String,
    name: String,
    version: String? = null,
): Parameters =
    ParametersBuilder()
        .apply {
            append("owner", owner)
            append("name", name)
            version?.let { append("version", it) }
        }.build()
