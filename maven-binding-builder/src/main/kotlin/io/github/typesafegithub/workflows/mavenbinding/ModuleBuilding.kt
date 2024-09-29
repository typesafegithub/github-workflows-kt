package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords

internal fun ActionCoords.buildModuleFile() =
    """
    {
      "formatVersion": "1.1",
      "component": {
        "group": "$owner",
        "module": "$mavenName",
        "version": "$version",
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
          "dependencies": [],
          "files": [
            {
              "name": "$mavenName-$version.jar",
              "url": "$mavenName-$version.jar",
              "size": 1
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
          "dependencies": [],
          "files": [
            {
              "name": "$mavenName-$version.jar",
              "url": "$mavenName-$version.jar",
              "size": 1
            }
          ]
        }
      ]
    }
    """.trimIndent()
