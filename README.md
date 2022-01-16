# GitHub Actions Kotlin DSL

Work in progress!

The goal is to be able to describe GH Actions in Kotlin with all its perks, like:

```kotlin
workflow(
    name = "Test workflow",
    on = listOf(Push),
) {
    job(
        name = "test_job",
        runsOn = UbuntuLatest,
    ) {
        uses(
            name = "Check out",
            action = Checkout(),
        )

        run(
            name = "Hello world!",
            command = "echo 'hello!'",
        )
    }
}
```

# Installation

In a Kotlin script:

```kotlin
#!/usr/bin/env kotlin

@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.1.0")
```

with Gradle:

```
repositories {
   mavenCentral()
}

dependencies {
    implementation("it.krzeminski:github-actions-kotlin-dsl:0.1.0")
}
```
