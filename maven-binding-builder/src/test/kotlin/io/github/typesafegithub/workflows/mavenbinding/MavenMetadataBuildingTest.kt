package io.github.typesafegithub.workflows.mavenbinding

import arrow.core.Either
import arrow.core.right
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.micrometer.core.instrument.MeterRegistry
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

            val xml =
                bindingsServerRequest.buildMavenMetadataFile(
                    githubAuthToken = "SOME_TOKEN",
                    fetchAvailableVersions = fetchAvailableVersions,
                )

            xml shouldBe
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <metadata>
                  <groupId>owner</groupId>
                  <artifactId>name</artifactId>
                  <versioning>
                    <latest>v2</latest>
                    <release>v2</release>
                    <versions>
                      <version>v2</version>
                      <version>v1</version>
                    </versions>
                    <lastUpdated>20240501000000</lastUpdated>
                  </versioning>
                </metadata>
                """.trimIndent()
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

            val xml =
                bindingsServerRequest.buildMavenMetadataFile(
                    githubAuthToken = "SOME_TOKEN",
                    fetchAvailableVersions = fetchAvailableVersions,
                )

            xml.shouldBeNull()
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

            val xml =
                bindingsServerRequest.buildMavenMetadataFile(
                    githubAuthToken = "SOME_TOKEN",
                    fetchAvailableVersions = fetchAvailableVersions,
                )

            xml.shouldBeNull()
        }

        (SignificantVersion.entries - FULL).forEach { significantVersion ->
            test("significant version $significantVersion requested") {
                // Given
                val fetchAvailableVersions: suspend (
                    String,
                    String,
                    String?,
                    MeterRegistry?,
                ) -> Either<String, List<Version>> = { owner, name, _, _ ->
                    listOf(
                        Version(
                            version = "v3-beta",
                            sha = "1",
                            dateProvider = { ZonedDateTime.parse("2024-07-01T00:00:00Z") },
                        ),
                        Version(
                            version = "v2",
                            sha = "2",
                            dateProvider = { ZonedDateTime.parse("2024-05-01T00:00:00Z") },
                        ),
                        Version(
                            version = "v1",
                            sha = "3",
                            dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.1",
                            sha = "4",
                            dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.1.0",
                            sha = "5",
                            dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.0.1",
                            sha = "6",
                            dateProvider = { ZonedDateTime.parse("2024-03-05T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.0",
                            sha = "7",
                            dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") },
                        ),
                        Version(
                            version = "v1.0.0",
                            sha = "8",
                            dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") },
                        ),
                    ).right()
                }

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
                        )

                xml shouldBe
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <metadata>
                      <groupId>owner</groupId>
                      <artifactId>name___$significantVersion</artifactId>
                      <versioning>
                        <latest>v2${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__2" else ""}</latest>
                        <release>v2${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__2" else ""}</release>
                        <versions>
                          <version>v3-beta${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__1" else ""}</version>
                          <version>v2${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__2" else ""}</version>
                          <version>v1${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__3" else ""}</version>
                          <version>v1.1${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__4" else ""}</version>
                          <version>v1.1.0${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__5" else ""}</version>
                          <version>v1.0.1${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__6" else ""}</version>
                          <version>v1.0${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__7" else ""}</version>
                          <version>v1.0.0${if (significantVersion == SignificantVersion.COMMIT_LENIENT) "__8" else ""}</version>
                        </versions>
                        <lastUpdated>20240501000000</lastUpdated>
                      </versioning>
                    </metadata>
                    """.trimIndent()
            }
        }
    })
