package io.github.typesafegithub.workflows.mavenbinding

fun buildModuleFile(
    owner: String,
    name: String,
    version: String,
): String =
    """
    {
      "formatVersion": "1.1",
      "component": {
        "group": "$owner",
        "module": "$name",
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
              "name": "$name-$version.jar",
              "url": "$name-$version.jar",
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
              "name": "$name-$version.jar",
              "url": "$name-$version.jar",
              "size": 1
            }
          ]
        }
      ]
    }
    """.trimIndent()
