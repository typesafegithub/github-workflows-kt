package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.fullName
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint

internal const val LATEST_RELASED_LIBRARY_VERSION = "3.7.0"

internal fun ActionCoords.buildPomFile() =
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <modelVersion>4.0.0</modelVersion>
      <groupId>$owner</groupId>
      <artifactId>$name</artifactId>
      <version>$version</version>
      <name>$fullName</name>
      <description>Auto-generated binding for $prettyPrint.</description>
      <url>https://github.com/$owner/$name</url>
      <scm>
        <connection>scm:git:git://github.com/$owner/$name.git/</connection>
        <developerConnection>scm:git:ssh://github.com:$owner/$name.git</developerConnection>
        <url>https://github.com/$owner/$name.git</url>
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
