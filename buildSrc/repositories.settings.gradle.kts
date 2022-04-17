@Suppress("UnstableApiUsage") // centralised repository definitions are incubating
dependencyResolutionManagement {

  repositories {
    mavenCentral()
    jitpack()
    gradlePluginPortal()
  }
  pluginManagement {
    repositories {
      jitpack()
      gradlePluginPortal()
      mavenCentral()
    }
  }
}

fun RepositoryHandler.jitpack() {
  maven("https://jitpack.io")
}
