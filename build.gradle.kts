plugins {
    base

    // Publishing.
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0" // Needs to be applied to the root project.
}

nexusPublishing {
    repositories {
        sonatype()
    }
    packageGroup.set("io.github.typesafegithub")
}
