package io.github.typesafegithub.workflows.mavenbinding

internal const val LATEST_RELASED_LIBRARY_VERSION = "3.0.1"

internal fun buildPomFile(
    owner: String,
    name: String,
    version: String,
): String {
    val nameForRepo = name.substringBefore("/")
    return """
        <?xml version="1.0" encoding="UTF-8"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <modelVersion>4.0.0</modelVersion>
          <groupId>$owner</groupId>
          <artifactId>$name</artifactId>
          <version>$version</version>
          <name>$name</name>
          <description>Auto-generated binding for $owner/$name@$version.</description>
          <url>https://github.com/$owner/$nameForRepo</url>
          <scm>
            <connection>scm:git:git://github.com/$owner/$nameForRepo.git/</connection>
            <developerConnection>scm:git:ssh://github.com:$owner/$nameForRepo.git</developerConnection>
            <url>https://github.com/$owner/$nameForRepo.git</url>
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
}
