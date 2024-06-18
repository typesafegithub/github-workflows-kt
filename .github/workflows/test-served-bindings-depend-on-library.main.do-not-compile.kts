#!/usr/bin/env kotlin
@file:Repository("http://localhost:8080")
@file:DependsOn("actions:checkout:v4")

import io.github.typesafegithub.workflows.actions.actions.Checkout

Checkout()
