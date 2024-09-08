package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.COMMIT_LENIENT
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MAJOR
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MINOR
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V1
import io.github.typesafegithub.workflows.mavenbinding.BindingsServerRequest
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withContexts
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder

class RequestParsingTest :
    FunSpec(
        {
            withContexts(
                nameFn = { """version prefix "${it.second}", version suffix "${it.third}"""" },
                ts =
                    sequence {
                        yield(Triple(V1, "", ""))
                        BindingVersion.entries.forEach {
                            yield(
                                Triple(
                                    it,
                                    "binding_version_${it}___",
                                    if (it.isExperimental) "-beta" else "",
                                ),
                            )
                        }
                        yield(
                            Triple(
                                null,
                                "binding_version_v${
                                    BindingVersion
                                        .entries
                                        .last()
                                        .name
                                        .substringAfter('V')
                                        .toInt() + 1
                                }___",
                                "",
                            ),
                        )
                        yield(Triple(null, "binding-version_v1___", ""))
                    },
            ) { (bindingVersion, versionPrefix, versionSuffix) ->
                context("parseRequest") {
                    test("parses owner/name with version and no path, FULL by default") {
                        val parameters =
                            createParameters(owner = "o", name = "act", version = "${versionPrefix}v1$versionSuffix")

                        if (bindingVersion == null) {
                            parameters.parseRequest(extractVersion = true).shouldBeNull()
                        } else {
                            parameters.parseRequest(extractVersion = true) shouldBe
                                BindingsServerRequest(
                                    rawName = "act",
                                    rawVersion = "${versionPrefix}v1$versionSuffix",
                                    bindingVersion = bindingVersion,
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
                    }

                    test("parses owner/name with path and significant version suffix") {
                        val parameters =
                            createParameters(
                                owner = "o",
                                name = "act__p1__p2___major",
                                version = "${versionPrefix}v9$versionSuffix",
                            )

                        if (bindingVersion == null) {
                            parameters.parseRequest(extractVersion = true).shouldBeNull()
                        } else {
                            parameters.parseRequest(extractVersion = true) shouldBe
                                BindingsServerRequest(
                                    rawName = "act__p1__p2___major",
                                    rawVersion = "${versionPrefix}v9$versionSuffix",
                                    bindingVersion = bindingVersion,
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
                    }

                    test("when extractVersion=false, version field is set to 'irrelevant'") {
                        val parameters = createParameters(owner = "o", name = "act___minor")

                        parameters.parseRequest(extractVersion = false) shouldBe
                            BindingsServerRequest(
                                rawName = "act___minor",
                                rawVersion = null,
                                bindingVersion = V1,
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
                                bindingVersion = V1,
                                actionCoords =
                                    ActionCoords(
                                        owner = "o",
                                        name = "act",
                                        version = "irrelevant",
                                        significantVersion = FULL,
                                        path = "_weird",
                                    ),
                            )
                    }

                    test("handles name with underscores/hyphens and path segments") {
                        val parameters =
                            createParameters(
                                owner = "o",
                                name = "my_action-name__dir_one__dir-two___minor",
                                version = "${versionPrefix}v2$versionSuffix",
                            )

                        if (bindingVersion == null) {
                            parameters.parseRequest(extractVersion = true).shouldBeNull()
                        } else {
                            parameters.parseRequest(extractVersion = true) shouldBe
                                BindingsServerRequest(
                                    rawName = "my_action-name__dir_one__dir-two___minor",
                                    rawVersion = "${versionPrefix}v2$versionSuffix",
                                    bindingVersion = bindingVersion,
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
                    }

                    test("parses commit_lenient syntax") {
                        val parameters =
                            createParameters(
                                owner = "o",
                                name = "act___commit_lenient",
                                version =
                                    "${versionPrefix}v1.2.3__323898970401d85df44b3324a610af9a862d54b3$versionSuffix",
                            )

                        if (bindingVersion == null) {
                            parameters.parseRequest(extractVersion = true).shouldBeNull()
                        } else {
                            parameters.parseRequest(extractVersion = true) shouldBe
                                BindingsServerRequest(
                                    rawName = "act___commit_lenient",
                                    rawVersion =
                                        "${versionPrefix}v1.2.3__323898970401d85df44b3324a610af9a862d54b3" +
                                            versionSuffix,
                                    bindingVersion = bindingVersion,
                                    actionCoords =
                                        ActionCoords(
                                            owner = "o",
                                            name = "act",
                                            version = "323898970401d85df44b3324a610af9a862d54b3",
                                            significantVersion = COMMIT_LENIENT,
                                            comment = "v1.2.3",
                                            versionForTypings = "v1.2.3",
                                            path = null,
                                        ),
                                )
                        }
                    }

                    test("parses commit_lenient syntax and no commit hash given") {
                        val parameters =
                            createParameters(
                                owner = "o",
                                name = "act___commit_lenient",
                                version = "${versionPrefix}v1.2.3$versionSuffix",
                            )

                        parameters.parseRequest(extractVersion = true).shouldBeNull()
                    }
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
