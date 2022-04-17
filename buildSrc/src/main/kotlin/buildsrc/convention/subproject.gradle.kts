package buildsrc.convention

import org.gradle.kotlin.dsl.base

plugins {
  base
}

if (project != rootProject) {
  project.group = rootProject.group
  project.version = rootProject.version
}
