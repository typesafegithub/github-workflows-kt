package io.github.typesafegithub.workflows.jitbindingserver

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MAJOR
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MINOR
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder

class ActionCoordsTest :
    FunSpec(
        {
            test("extractActionCoords parses owner/name with version and no path, FULL by default") {
                val parameters = createParameters(owner = "o", name = "act", version = "v1")

                parameters.extractActionCoords(true) shouldBe
                    ActionCoords(
                        owner = "o",
                        name = "act",
                        version = "v1",
                        significantVersion = FULL,
                        path = null,
                    )
            }

            test("extractActionCoords parses owner/name with path and significant version suffix") {
                val parameters = createParameters(owner = "o", name = "act__p1__p2___major", version = "v9")

                parameters.extractActionCoords(true) shouldBe
                    ActionCoords(
                        owner = "o",
                        name = "act",
                        version = "v9",
                        significantVersion = MAJOR,
                        path = "p1/p2",
                    )
            }

            test("when extractVersion=false, version field is set to 'irrelevant'") {
                val parameters = createParameters(owner = "o", name = "act___minor")

                parameters.extractActionCoords(false) shouldBe
                    ActionCoords(
                        owner = "o",
                        name = "act",
                        version = "irrelevant",
                        significantVersion = MINOR,
                        path = null,
                    )
            }

            test("unknown significant version part falls back to FULL") {
                val parameters = createParameters(owner = "o", name = "act___weird")

                parameters.extractActionCoords(false) shouldBe
                    ActionCoords(
                        owner = "o",
                        name = "act",
                        version = "irrelevant",
                        significantVersion = FULL,
                        path = null,
                    )
            }

            test("handles name with underscores/hyphens and path segments") {
                val parameters =
                    createParameters(owner = "o", name = "my_action-name__dir_one__dir-two___minor", version = "v2")

                parameters.extractActionCoords(true) shouldBe
                    ActionCoords(
                        owner = "o",
                        name = "my_action-name",
                        version = "v2",
                        significantVersion = MINOR,
                        path = "dir_one/dir-two",
                    )
            }

            test("empty path part yields no path") {
                val parameters = createParameters(owner = "o", name = "act__\u200B___major", version = "v3") // zero-width space as noise

                parameters.extractActionCoords(true) shouldBe
                    ActionCoords(
                        owner = "o",
                        name = "act",
                        version = "v3",
                        significantVersion = MAJOR,
                        path = null,
                    )
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
