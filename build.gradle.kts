plugins {
    kotlin("jvm") version "1.6.21" apply false
    kotlin("plugin.serialization") version "1.6.20" apply false

    // Code quality.
    id("io.gitlab.arturbosch.detekt") version "1.20.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1" apply false
}
