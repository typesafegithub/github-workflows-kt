plugins {
    // Publishing.
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0" // Needs to be applied to the root project.
}

group = "it.krzeminski"
version = "0.13.0"

nexusPublishing {
    repositories {
        sonatype()
    }
    packageGroup.set("it.krzeminski")
}

tasks.wrapper {
    gradleVersion = "7.4.2"
}
