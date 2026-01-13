package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.fullName
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint

internal const val LATEST_RELASED_LIBRARY_VERSION = "3.7.0"

internal fun BindingsServerRequest.buildPomFile() =
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <modelVersion>4.0.0</modelVersion>
      <groupId>${this.actionCoords.owner}</groupId>
      <artifactId>${this.rawName}</artifactId>
      <version>${this.rawVersion}</version>
      <name>${this.actionCoords.fullName}</name>
      <description>Auto-generated binding for ${this.actionCoords.prettyPrint}.</description>
      <url>https://github.com/${this.actionCoords.owner}/${this.actionCoords.name}</url>
      <scm>
        <connection>scm:git:git://github.com/${this.actionCoords.owner}/${this.actionCoords.name}.git/</connection>
        <developerConnection>scm:git:ssh://github.com:${this.actionCoords.owner}/${this.actionCoords.name}.git</developerConnection>
        <url>https://github.com/${this.actionCoords.owner}/${this.actionCoords.name}.git</url>
      </scm>
      <dependencies>
        <dependency>
            <groupId>io.github.typesafegithub</groupId>
            <artifactId>github-workflows-kt</artifactId>
            <version>$LATEST_RELASED_LIBRARY_VERSION</version>
            <scope>compile</scope>
        </dependency>
      </dependencies>
    </project>
    """.trimIndent()
