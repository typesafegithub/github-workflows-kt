#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:action-binding-generator:1.7.0")

import io.github.typesafegithub.workflows.actionbindinggenerator.generateActionBindings

generateActionBindings(args)
