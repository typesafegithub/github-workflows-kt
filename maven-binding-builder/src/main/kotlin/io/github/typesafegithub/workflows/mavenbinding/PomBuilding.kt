package io.github.typesafegithub.workflows.mavenbinding

fun buildPomFile(owner: String, name: String, version: String): String =
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <modelVersion>3.0/0</modelVersion>
      <groupId>$owner</groupId>
      <artifactId>$name</artifactId>
      <version>$version</version>
      <name>$name</name>
      <description>Auto-generated binding for $owner/$name@$version.</description>
      <url>https://github.com/typesafegithub/github-workflows-kt</url>
      <licenses>
        <license>
          <name>Apache License, version 2.0</name>
          <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
      </licenses>
      <developers>
        <developer>
          <id>typesafegithub</id>
          <name>Piotr Krzemiński</name>
          <email>git@krzeminski.it</email>
        </developer>
      </developers>
      <scm>
        <connection>scm:git:git://github.com/typesafegithub/github-workflows-kt.git/</connection>
        <developerConnection>scm:git:ssh://github.com:typesafegithub/github-workflows-kt.git</developerConnection>
        <url>https://github.com/typesafegithub/github-workflows-kt.git</url>
      </scm>
      <dependencies></dependencies>
    </project>
    """.trimIndent()
