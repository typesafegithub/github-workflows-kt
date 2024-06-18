#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:Repository("http://localhost:8080")
@file:DependsOn("actions:checkout:v4")

import io.github.typesafegithub.workflows.actions.actions.Checkout
println("after import")

println("before instantiating Checkout")
Checkout()
println("after instantiating Checkout")
