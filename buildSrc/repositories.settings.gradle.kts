import java.net.URI

// A settings.gradle.kts plugin for defining shared repositories used by both buildSrc and the root project

@Suppress("UnstableApiUsage") // centralised repository definitions are incubating
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }

        // It has to be defined here because preferring repositories config in settings apparently removes the below
        // additions done by Kotlin/JS plugin.
        // Tracked in https://youtrack.jetbrains.com/issue/KT-51379
        ivy {
            name = "Node distributions"
            url = URI("https://nodejs.org/dist")
            patternLayout {
                artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]")
            }
            metadataSources { artifact() }
            content { includeModule("org.nodejs", "node") }
        }
        ivy {
            name = "Yarn distributions"
            url = URI("https://github.com/yarnpkg/yarn/releases/download")
            patternLayout {
                artifact("v[revision]/[artifact](-v[revision]).[ext]")
            }
            metadataSources { artifact() }
            content { includeModule("com.yarnpkg", "yarn") }
        }
    }

    pluginManagement {
        repositories {
            gradlePluginPortal()
            mavenCentral()
        }
    }
}
