#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:action-binding-generator:1.7.1-20231211.115819-7")

import io.github.typesafegithub.workflows.actionbindinggenerator.generateActionBindings

generateActionBindings(args, __FILE__.toPath())
