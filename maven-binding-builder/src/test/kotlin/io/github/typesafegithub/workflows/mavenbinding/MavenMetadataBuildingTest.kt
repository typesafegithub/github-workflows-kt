package io.github.typesafegithub.workflows.mavenbinding

import arrow.core.Either
import arrow.core.right
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.COMMIT_LENIENT
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import java.time.ZonedDateTime

class MavenMetadataBuildingTest :
    FunSpec({
        val bindingsServerRequest =
            BindingsServerRequest(
                rawName = "name",
                rawVersion = null,
                actionCoords =
                    ActionCoords(
                        owner = "owner",
                        name = "name",
                        version = "irrelevant",
                    ),
            )

        test("various kinds of versions available") {
            // Given
            val fetchAvailableVersions: suspend (
                String,
                String,
                String?,
                MeterRegistry?,
            ) -> Either<String, List<Version>> = { _, _, _, _ ->
                listOf(
                    Version(version = "v3-beta", dateProvider = { ZonedDateTime.parse("2024-07-01T00:00:00Z") }),
                    Version(version = "v2", dateProvider = { ZonedDateTime.parse("2024-05-01T00:00:00Z") }),
                    Version(version = "v1", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.1", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.1.0", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.0.1", dateProvider = { ZonedDateTime.parse("2024-03-05T00:00:00Z") }),
                    Version(version = "v1.0", dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") }),
                    Version(version = "v1.0.0", dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") }),
                ).right()
            }

            var prefetchedCoords: Collection<BindingsServerRequest>? = null
            val xml =
                bindingsServerRequest.buildMavenMetadataFile(
                    githubAuthToken = "SOME_TOKEN",
                    fetchAvailableVersions = fetchAvailableVersions,
                    prefetchBindingArtifacts = { prefetchedCoords = it },
                )

            xml shouldBe
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <metadata>
                  <groupId>owner</groupId>
                  <artifactId>name</artifactId>
                  <versioning>
                    <latest>binding_version_${BindingVersion.entries.last()}___v2</latest>
                    <release>binding_version_${BindingVersion.entries.last()}___v2</release>
                    <versions>
                      <version>v2</version>
${
                    BindingVersion.entries.map {
                        "                      <version>binding_version_${it}___v2</version>"
                    }.joinToString(separator = "\n")
                }
                      <version>v1</version>
${
                    BindingVersion.entries.map {
                        "                      <version>binding_version_${it}___v1</version>"
                    }.joinToString(separator = "\n")
                }
                    </versions>
                    <lastUpdated>20240501000000</lastUpdated>
                  </versioning>
                </metadata>
                """.trimIndent()

            prefetchedCoords shouldNotBe null
            prefetchedCoords shouldContainExactlyInAnyOrder
                sequence {
                    yield(
                        bindingsServerRequest.copy(
                            rawName = "name",
                            rawVersion = "v1",
                            actionCoords =
                                bindingsServerRequest.actionCoords.copy(
                                    version = "v1",
                                    versionForTypings = "v1",
                                ),
                        ),
                    )
                    yield(
                        bindingsServerRequest.copy(
                            rawName = "name",
                            rawVersion = "v2",
                            actionCoords =
                                bindingsServerRequest.actionCoords.copy(
                                    version = "v2",
                                    versionForTypings = "v2",
                                ),
                        ),
                    )
                    BindingVersion.entries.forEach { bindingVersion ->
                        yield(
                            bindingsServerRequest.copy(
                                rawName = "name",
                                rawVersion = "binding_version_${bindingVersion}___v1",
                                bindingVersion = bindingVersion,
                                actionCoords =
                                    bindingsServerRequest.actionCoords.copy(
                                        version = "v1",
                                        versionForTypings = "v1",
                                    ),
                            ),
                        )
                        yield(
                            bindingsServerRequest.copy(
                                rawName = "name",
                                rawVersion = "binding_version_${bindingVersion}___v2",
                                bindingVersion = bindingVersion,
                                actionCoords =
                                    bindingsServerRequest.actionCoords.copy(
                                        version = "v2",
                                        versionForTypings = "v2",
                                    ),
                            ),
                        )
                    }
                }.toList()
        }

        test("no major versions") {
            // Given
            val fetchAvailableVersions: suspend (
                String,
                String,
                String?,
                MeterRegistry?,
            ) -> Either<String, List<Version>> = { _, _, _, _ ->
                listOf(
                    Version(version = "v1.1", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.1.0", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.0.1", dateProvider = { ZonedDateTime.parse("2024-03-05T00:00:00Z") }),
                    Version(version = "v1.0", dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") }),
                    Version(version = "v1.0.0", dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") }),
                ).right()
            }

            var prefetchedCoords: Collection<BindingsServerRequest>? = null
            val xml =
                bindingsServerRequest.buildMavenMetadataFile(
                    githubAuthToken = "SOME_TOKEN",
                    fetchAvailableVersions = fetchAvailableVersions,
                    prefetchBindingArtifacts = { prefetchedCoords = it },
                )

            xml.shouldBeNull()

            prefetchedCoords shouldNotBeNull {
                size shouldBe 0
            }
        }

        test("no versions available") {
            // Given
            val fetchAvailableVersions: suspend (
                String,
                String,
                String?,
                MeterRegistry?,
            ) -> Either<String, List<Version>> = { _, _, _, _ ->
                emptyList<Version>().right()
            }

            var prefetchedCoords: Collection<BindingsServerRequest>? = null
            val xml =
                bindingsServerRequest.buildMavenMetadataFile(
                    githubAuthToken = "SOME_TOKEN",
                    fetchAvailableVersions = fetchAvailableVersions,
                    prefetchBindingArtifacts = { prefetchedCoords = it },
                )

            xml.shouldBeNull()

            prefetchedCoords shouldNotBeNull {
                size shouldBe 0
            }
        }

        (SignificantVersion.entries - FULL).forEach { significantVersion ->
            test("significant version $significantVersion requested") {
                // Given
                var availableVersions =
                    listOf(
                        Version(
                            version = "v3-beta",
                            shaProvider = { "1" },
                            dateProvider = { ZonedDateTime.parse("2024-07-01T00:00:00Z") },
                        ),
                        Version(
                            version = "v2",
                            shaProvider = { "2" },
                            dateProvider = { ZonedDateTime.parse("2024-05-01T00:00:00Z") },
                        ),
                        Version(
                            version = "v1",
                            shaProvider = { "3" },
                            dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.1",
                            shaProvider = { "4" },
                            dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.1.0",
                            shaProvider = { "5" },
                            dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.0.1",
                            shaProvider = { "6" },
                            dateProvider = { ZonedDateTime.parse("2024-03-05T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.0",
                            shaProvider = { "7" },
                            dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.0.0",
                            shaProvider = { "8" },
                            dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") },
                        ),
                    )
                val fetchAvailableVersions: suspend (
                    String,
                    String,
                    String?,
                    MeterRegistry?,
                ) -> Either<String, List<Version>> = { owner, name, _, _ ->
                    availableVersions.right()
                }

                var prefetchedCoords: Collection<BindingsServerRequest>? = null
                val xml =
                    bindingsServerRequest
                        .copy(
                            rawName = "name___$significantVersion",
                            actionCoords =
                                bindingsServerRequest.actionCoords.copy(
                                    significantVersion = significantVersion,
                                ),
                        ).buildMavenMetadataFile(
                            githubAuthToken = "SOME_TOKEN",
                            fetchAvailableVersions = fetchAvailableVersions,
                            prefetchBindingArtifacts = { prefetchedCoords = it },
                        )

                val commitLenient = significantVersion == COMMIT_LENIENT
                xml shouldBe
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <metadata>
                      <groupId>owner</groupId>
                      <artifactId>name___$significantVersion</artifactId>
                      <versioning>
                        <latest>binding_version_${BindingVersion.entries.last()}___v2${if (commitLenient) "__2" else ""}</latest>
                        <release>binding_version_${BindingVersion.entries.last()}___v2${if (commitLenient) "__2" else ""}</release>
                        <versions>
                          <version>v3-beta${if (commitLenient) "__1" else ""}</version>
${
                        BindingVersion.entries.map {
                            "                          <version>binding_version_${it}___v3-beta${if (commitLenient) "__1" else ""}</version>"
                        }.joinToString(separator = "\n")
                    }
                          <version>v2${if (commitLenient) "__2" else ""}</version>
${
                        BindingVersion.entries.map {
                            "                          <version>binding_version_${it}___v2${if (commitLenient) "__2" else ""}</version>"
                        }.joinToString(separator = "\n")
                    }
                          <version>v1${if (commitLenient) "__3" else ""}</version>
${
                        BindingVersion.entries.map {
                            "                          <version>binding_version_${it}___v1${if (commitLenient) "__3" else ""}</version>"
                        }.joinToString(separator = "\n")
                    }
                          <version>v1.1${if (commitLenient) "__4" else ""}</version>
${
                        BindingVersion.entries.map {
                            "                          <version>binding_version_${it}___v1.1${if (commitLenient) "__4" else ""}</version>"
                        }.joinToString(separator = "\n")
                    }
                          <version>v1.1.0${if (commitLenient) "__5" else ""}</version>
${
                        BindingVersion.entries.map {
                            "                          <version>binding_version_${it}___v1.1.0${if (commitLenient) "__5" else ""}</version>"
                        }.joinToString(separator = "\n")
                    }
                          <version>v1.0.1${if (commitLenient) "__6" else ""}</version>
${
                        BindingVersion.entries.map {
                            "                          <version>binding_version_${it}___v1.0.1${if (commitLenient) "__6" else ""}</version>"
                        }.joinToString(separator = "\n")
                    }
                          <version>v1.0${if (commitLenient) "__7" else ""}</version>
${
                        BindingVersion.entries.map {
                            "                          <version>binding_version_${it}___v1.0${if (commitLenient) "__7" else ""}</version>"
                        }.joinToString(separator = "\n")
                    }
                          <version>v1.0.0${if (commitLenient) "__8" else ""}</version>
${
                        BindingVersion.entries.map {
                            "                          <version>binding_version_${it}___v1.0.0${if (commitLenient) "__8" else ""}</version>"
                        }.joinToString(separator = "\n")
                    }
                        </versions>
                        <lastUpdated>20240501000000</lastUpdated>
                      </versioning>
                    </metadata>
                    """.trimIndent()

                prefetchedCoords shouldNotBe null
                prefetchedCoords shouldContainExactlyInAnyOrder
                    flow {
                        availableVersions.map { it to it.getSha() }.forEach { (availableVersion, sha) ->
                            emit(
                                bindingsServerRequest.copy(
                                    rawName = "name___$significantVersion",
                                    rawVersion =
                                        "$availableVersion${if (commitLenient) "__${availableVersion.getSha()}" else ""}",
                                    actionCoords =
                                        bindingsServerRequest.actionCoords.copy(
                                            version =
                                                if (commitLenient) availableVersion.getSha()!! else "$availableVersion",
                                            comment = if (commitLenient) "$availableVersion" else null,
                                            significantVersion = significantVersion,
                                            versionForTypings = "$availableVersion",
                                        ),
                                ),
                            )
                            BindingVersion.entries.forEach { bindingVersion ->
                                emit(
                                    bindingsServerRequest.copy(
                                        rawName = "name___$significantVersion",
                                        rawVersion =
                                            "binding_version_${bindingVersion}___$availableVersion${if (commitLenient) "__${availableVersion.getSha()}" else ""}",
                                        bindingVersion = bindingVersion,
                                        actionCoords =
                                            bindingsServerRequest.actionCoords.copy(
                                                version =
                                                    if (commitLenient) {
                                                        availableVersion.getSha()!!
                                                    } else {
                                                        "$availableVersion"
                                                    },
                                                comment = if (commitLenient) "$availableVersion" else null,
                                                significantVersion = significantVersion,
                                                versionForTypings = "$availableVersion",
                                            ),
                                    ),
                                )
                            }
                        }
                    }.toList()
            }
        }
    })
