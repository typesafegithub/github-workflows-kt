package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.ZonedDateTime

class MavenMetadataBuildingTest :
    FunSpec({
        val actionCoords =
            ActionCoords(
                owner = "owner",
                name = "name",
                version = "irrelevant",
            )

        test("various kinds of versions available") {
            // Given
            val fetchAvailableVersions: suspend (String, String, String?) -> List<Version> = { owner, name, _ ->
                listOf(
                    Version(version = "v3-beta", dateProvider = { ZonedDateTime.parse("2024-07-01T00:00:00Z") }),
                    Version(version = "v2", dateProvider = { ZonedDateTime.parse("2024-05-01T00:00:00Z") }),
                    Version(version = "v1", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.1", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.1.0", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.0.1", dateProvider = { ZonedDateTime.parse("2024-03-05T00:00:00Z") }),
                    Version(version = "v1.0", dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") }),
                    Version(version = "v1.0.0", dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") }),
                )
            }

            val xml =
                actionCoords.buildMavenMetadataFile(
                    githubToken = "SOME_TOKEN",
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
            val fetchAvailableVersions: suspend (String, String, String?) -> List<Version> = { owner, name, _ ->
                listOf(
                    Version(version = "v1.1", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.1.0", dateProvider = { ZonedDateTime.parse("2024-03-07T00:00:00Z") }),
                    Version(version = "v1.0.1", dateProvider = { ZonedDateTime.parse("2024-03-05T00:00:00Z") }),
                    Version(version = "v1.0", dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") }),
                    Version(version = "v1.0.0", dateProvider = { ZonedDateTime.parse("2024-03-01T00:00:00Z") }),
                )
            }

            val xml =
                actionCoords.buildMavenMetadataFile(
                    githubToken = "SOME_TOKEN",
                    fetchAvailableVersions = fetchAvailableVersions,
                )

            xml.shouldBeNull()
        }

        test("no versions available") {
            // Given
            val fetchAvailableVersions: suspend (String, String, String?) -> List<Version> = { owner, name, _ ->
                emptyList()
            }

            val xml =
                actionCoords.buildMavenMetadataFile(
                    githubToken = "SOME_TOKEN",
                    fetchAvailableVersions = fetchAvailableVersions,
                )

            xml.shouldBeNull()
        }
    })
