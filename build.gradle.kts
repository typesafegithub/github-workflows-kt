plugins {
    kotlin("jvm") version "1.7.0" apply false
    kotlin("plugin.serialization") version "1.7.10" apply false

    // Code quality.
    id("io.gitlab.arturbosch.detekt") version "1.20.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0" apply false

    // Publishing.
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0" // Needs to be applied to the root project.
}

nexusPublishing {
    repositories {
        sonatype()
    }
    packageGroup.set("it.krzeminski")
}
