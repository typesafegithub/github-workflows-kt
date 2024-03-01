#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:action-binding-generator:1.12.0")

import io.github.typesafegithub.workflows.actionbindinggenerator.annotations.ExperimentalClientSideBindings
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.generateActionBindings

@OptIn(ExperimentalClientSideBindings::class)
generateActionBindings(args = args, sourceFile = __FILE__.toPath())
