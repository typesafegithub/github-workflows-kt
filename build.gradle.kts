plugins {
    base

    // Publishing.
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0" // Needs to be applied to the root project.
}

group = "io.github.typesafegithub"
version = "3.7.0"

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
    packageGroup.set("io.github.typesafegithub")
}

val setIsSnapshotFlagInGithubOutput by tasks.registering {
    // This property of a project needs to be resolved before the 'doLast' block because otherwise, Gradle
    // configuration cache cannot be used.
    val version = version

    doLast {
        val filePath = System.getenv("GITHUB_OUTPUT") ?: error("Expected GITHUB_OUTPUT variable to be set!")
        val isSnapshot = version.toString().endsWith("-SNAPSHOT")
        File(filePath).appendText("is-snapshot=$isSnapshot\n")
    }
}
