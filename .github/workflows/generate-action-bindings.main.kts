#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:action-binding-generator:1.4.1-20231109.091111-23")

import io.github.typesafegithub.workflows.actionbindinggenerator.generateActionBindings

generateActionBindings(args)
