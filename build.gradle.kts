plugins {
    base

    // Publishing.
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0" // Needs to be applied to the root project.
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
    packageGroup.set("io.github.typesafegithub")
}
