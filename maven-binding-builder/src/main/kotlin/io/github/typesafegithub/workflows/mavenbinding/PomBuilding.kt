package io.github.typesafegithub.workflows.mavenbinding

internal const val LATEST_RELASED_LIBRARY_VERSION = "2.1.0"

internal fun buildPomFile(
    owner: String,
    name: String,
    version: String,
): String =
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <modelVersion>3.0/0</modelVersion>
      <groupId>$owner</groupId>
      <artifactId>$name</artifactId>
      <version>$version</version>
      <name>$name</name>
      <description>Auto-generated binding for $owner/$name@$version.</description>
      <url>https://github.com/$owner/$name</url>
      <scm>
        <connection>scm:git:git://github.com/$owner/$name.git/</connection>
        <developerConnection>scm:git:ssh://github.com:$owner/$name.git</developerConnection>
        <url>https://github.com/$owner/$name.git</url>
      </scm>
      <repositories>
        <repository>
          <name>Maven Central</name>
          <id>maven-central</id>
          <url>https://repo.maven.apache.org/maven2/</url>
          <layout>default</layout>
        </repository>
      </repositories>
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
