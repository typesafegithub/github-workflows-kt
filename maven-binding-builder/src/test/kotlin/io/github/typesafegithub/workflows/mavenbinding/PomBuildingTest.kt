package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.COMMIT_LENIENT
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PomBuildingTest :
    FunSpec({
        SignificantVersion.entries.forEach { significantVersion ->
            test("significant version $significantVersion requested") {
                // given
                val commitLenient = significantVersion == COMMIT_LENIENT
                var nameSuffix = if (significantVersion == FULL) "" else "___$significantVersion"
                var versionSuffix = if (commitLenient) "__commit-sha" else ""
                var prettyPrintSuffix =
                    when (significantVersion) {
                        FULL -> ""
                        COMMIT_LENIENT -> " with $significantVersion version and comment 'v1.2.3'"
                        else -> " with $significantVersion version"
                    }
                var version = if (commitLenient) "commit-sha" else "v1.2.3"

                val bindingsServerRequest =
                    BindingsServerRequest(
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
                val xml = bindingsServerRequest.buildPomFile()

                xml shouldBe
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                      <modelVersion>4.0.0</modelVersion>
                      <groupId>owner</groupId>
                      <artifactId>name$nameSuffix</artifactId>
                      <version>v1.2.3$versionSuffix</version>
                      <name>name</name>
                      <description>Auto-generated binding for owner/name$prettyPrintSuffix@$version.</description>
                      <url>https://github.com/owner/name</url>
                      <scm>
                        <connection>scm:git:git://github.com/owner/name.git/</connection>
                        <developerConnection>scm:git:ssh://github.com:owner/name.git</developerConnection>
                        <url>https://github.com/owner/name.git</url>
                      </scm>
                      <dependencies>
                        <dependency>
                            <groupId>io.github.typesafegithub</groupId>
                            <artifactId>github-workflows-kt</artifactId>
                            <version>3.7.0</version>
                            <scope>compile</scope>
                        </dependency>
                      </dependencies>
                    </project>
                    """.trimIndent()
            }
        }
    })
