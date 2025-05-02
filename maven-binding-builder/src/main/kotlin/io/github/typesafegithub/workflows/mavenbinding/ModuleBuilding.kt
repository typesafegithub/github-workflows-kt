package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords

internal fun ActionCoords.buildModuleFile(
    mainJarSize: Int,
    mainJarMd5Checksum: String,
    mainJarSha1Checksum: String,
    mainJarSha256Checksum: String,
    mainJarSha512Checksum: String,
    sourcesJarSize: Int,
    sourcesJarMd5Checksum: String,
    sourcesJarSha1Checksum: String,
    sourcesJarSha256Checksum: String,
    sourcesJarSha512Checksum: String,
) = """
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
          "files": [
            {
              "name": "$mavenName-$version.jar",
              "url": "$mavenName-$version.jar",
              "size": $mainJarSize,
              "sha512": "$mainJarSha512Checksum",
              "sha256": "$mainJarSha256Checksum",
              "sha1": "$mainJarSha1Checksum",
              "md5": "$mainJarMd5Checksum"
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
                "requires": "$LATEST_RELASED_LIBRARY_VERSION"
              }
            }
          ],
          "files": [
            {
              "name": "$mavenName-$version.jar",
              "url": "$mavenName-$version.jar",
              "size": $mainJarSize,
              "sha512": "$mainJarSha512Checksum",
              "sha256": "$mainJarSha256Checksum",
              "sha1": "$mainJarSha1Checksum",
              "md5": "$mainJarMd5Checksum"
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
              "name": "$mavenName-$version-sources.jar",
              "url": "$mavenName-$version-sources.jar",
              "size": $sourcesJarSize,
              "sha512": "$sourcesJarSha512Checksum",
              "sha256": "$sourcesJarSha256Checksum",
              "sha1": "$sourcesJarSha1Checksum",
              "md5": "$sourcesJarMd5Checksum"
            }
          ]
        }
      ]
    }
    """.trimIndent()
