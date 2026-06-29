package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.COMMIT_LENIENT
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ModuleBuildingTest :
    FunSpec({
        SignificantVersion.entries.forEach { significantVersion ->
            test("significant version $significantVersion requested") {
                val commitLenient = significantVersion == COMMIT_LENIENT
                var nameSuffix = if (significantVersion == FULL) "" else "___$significantVersion"
                var versionSuffix = if (commitLenient) "__commit-sha" else ""
                var version = if (commitLenient) "commit-sha" else "v1.2.3"

                val bindingsServerRequest =
                    BindingsServerRequest(
                        rawOwner = "owner",
                        rawName = "name$nameSuffix",
                        rawVersion = "v1.2.3$versionSuffix",
                        actionCoords =
                            ActionCoords(
                                owner = "owner",
                                name = "name",
                                version = version,
                                comment = if (commitLenient) "v1.2.3" else null,
                                significantVersion = significantVersion,
                            ),
                    )
                val json =
                    bindingsServerRequest.buildModuleFile(
                        mainJarSize = 1,
                        mainJarMd5Checksum = "main-md5",
                        mainJarSha1Checksum = "main-sha1",
                        mainJarSha256Checksum = "main-sha256",
                        mainJarSha512Checksum = "main-sha512",
                        sourcesJarSize = 2,
                        sourcesJarMd5Checksum = "sources-md5",
                        sourcesJarSha1Checksum = "sources-sha1",
                        sourcesJarSha256Checksum = "sources-sha256",
                        sourcesJarSha512Checksum = "sources-sha512",
                    )

                json shouldBe
                    """
                    {
                      "formatVersion": "1.1",
                      "component": {
                        "group": "owner",
                        "module": "name$nameSuffix",
                        "version": "v1.2.3$versionSuffix",
                        "attributes": {
                          "org.gradle.status": "release"
                        }
                      },
                      "createdBy": {
                        "gradle": {
                          "version": "8.7"
                        }
                      },
                      "variants": [
                        {
                          "name": "apiElements",
                          "attributes": {
                            "org.gradle.category": "library",
                            "org.gradle.dependency.bundling": "external",
                            "org.gradle.jvm.environment": "standard-jvm",
                            "org.gradle.jvm.version": 11,
                            "org.gradle.libraryelements": "jar",
                            "org.gradle.usage": "java-api",
                            "org.jetbrains.kotlin.platform.type": "jvm"
                          },
                          "files": [
                            {
                              "name": "name$nameSuffix-v1.2.3$versionSuffix.jar",
                              "url": "name$nameSuffix-v1.2.3$versionSuffix.jar",
                              "size": 1,
                              "sha512": "main-sha512",
                              "sha256": "main-sha256",
                              "sha1": "main-sha1",
                              "md5": "main-md5"
                            }
                          ]
                        },
                        {
                          "name": "runtimeElements",
                          "attributes": {
                            "org.gradle.category": "library",
                            "org.gradle.dependency.bundling": "external",
                            "org.gradle.jvm.environment": "standard-jvm",
                            "org.gradle.jvm.version": 11,
                            "org.gradle.libraryelements": "jar",
                            "org.gradle.usage": "java-runtime",
                            "org.jetbrains.kotlin.platform.type": "jvm"
                          },
                          "dependencies": [
                            {
                              "group": "io.github.typesafegithub",
                              "module": "github-workflows-kt",
                              "version": {
                                "requires": "3.7.0"
                              }
                            }
                          ],
                          "files": [
                            {
                              "name": "name$nameSuffix-v1.2.3$versionSuffix.jar",
                              "url": "name$nameSuffix-v1.2.3$versionSuffix.jar",
                              "size": 1,
                              "sha512": "main-sha512",
                              "sha256": "main-sha256",
                              "sha1": "main-sha1",
                              "md5": "main-md5"
                            }
                          ]
                        },
                        {
                          "name": "sourcesElements",
                          "attributes": {
                            "org.gradle.category": "documentation",
                            "org.gradle.dependency.bundling": "external",
                            "org.gradle.docstype": "sources",
                            "org.gradle.usage": "java-runtime"
                          },
                          "files": [
                            {
                              "name": "name$nameSuffix-v1.2.3$versionSuffix-sources.jar",
                              "url": "name$nameSuffix-v1.2.3$versionSuffix-sources.jar",
                              "size": 2,
                              "sha512": "sources-sha512",
                              "sha256": "sources-sha256",
                              "sha1": "sources-sha1",
                              "md5": "sources-md5"
                            }
                          ]
                        }
                      ]
                    }
                    """.trimIndent()
            }
        }
    })
