plugins {
    base
    // Publishing.
    id("io.github.gradle-nexus.publish-plugin") // Needs to be applied to the root project.

    // note: plugin versions are defined in ./buildSrc/build.gradle.kts
}

group = "it.krzeminski"
version = "0.23.0"

nexusPublishing {
    repositories {
        sonatype()
    }
    packageGroup.set("it.krzeminski")
}

tasks.wrapper {
    gradleVersion = "7.5"
}
