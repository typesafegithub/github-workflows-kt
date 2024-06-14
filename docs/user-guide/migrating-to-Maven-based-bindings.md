# Migrating to Maven-based bindings

!!! warning "Experimental"
    This feature is experimental. We're awaiting feedback and fixing remaining issues. Breaking changes may appear as
    well. You can give it a try in non-critical projects.

## Goal

Let's say you have the following workflow:

```kotlin
#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:<newest-version>")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradleV3
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Build",
    on = listOf(PullRequest()),
    sourceFile = __FILE__,
) {
    job(id = "build", runsOn = UbuntuLatest) {
        uses(action = CheckoutV4())
        uses(action = SetupJavaV3())
        uses(action = ActionsSetupGradleV3())
        run(
            name = "Build",
            command = "./gradlew build",
        )
    }
}
```

`CheckoutV4`, `SetupJavaV3` and `ActionsSetupGradleV3` come with the library - they are shipped together with the DSL.
It has a number of downsides:

* the library needs to be aware of a certain action, which requires the maintainer's attention
* getting new versions of the bindings happens is tied to the release cadence of the library, which is currently
  released monthly
* automatic updates via dependency update bots like Renovate or Dependabot aren't possible

We're going to switch to the new approach where each type-safe action binding is compiled and packaged in a JAR on
demand, and is then shipped as a separate Maven artifact from a custom Maven-compatible server.

## Required changes

The following changes are required in our example workflow:
   ```diff
     #!/usr/bin/env kotlin
   + @file:Repository("https://repo.maven.apache.org/maven2/")
     @file:DependsOn("io.github.typesafegithub:github-workflows-kt:<newest-version>")
   + @file:Repository("https://bindings.krzeminski.it")
   + @file:DependsOn("actions:checkout:v4")
   + @file:DependsOn("actions:setup-java:v3")
   + @file:DependsOn("gradle:actions__setup-gradle:v3")

   - import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
   + import io.github.typesafegithub.workflows.actions.actions.Checkout
   - import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
   + import io.github.typesafegithub.workflows.actions.actions.SetupJava
   - import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradleV3
   + import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
     import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
     import io.github.typesafegithub.workflows.domain.triggers.PullRequest
     import io.github.typesafegithub.workflows.dsl.workflow
     import io.github.typesafegithub.workflows.yaml.writeToFile

     workflow(
         name = "Build",
         on = listOf(PullRequest()),
         sourceFile = __FILE__,
     ) {
         job(id = "build", runsOn = UbuntuLatest) {
   -         uses(action = CheckoutV4())
   +         uses(action = Checkout())
   -         uses(action = SetupJavaV3())
   +         uses(action = SetupJava())
   -         uses(action = ActionsSetupGradleV3())
   +         uses(action = ActionsSetupGradle())
             run(
                 name = "Build",
                 command = "./gradlew build",
             )
         }
     }
   ```

Then regenerate your YAML to ensure there are no changes.

## Remarks

1. Top-level actions like `actions/checkout@v4` map to Maven artifacts like `actions:checkout:v4`. Nested actions like
   `gradle/actions/setup-gradle@v3` require replacing the slash in the middle of the compound name with `__` (double
   underscore) because slash is not allowed in Maven coordinates. Hence, we get `gradle:actions__setup-gradle:v3`.
1. So far, only major versions (e.g. `v3`) are supported. Support for full versions like `v1.2.3` will be added in the
   future.
1. If a given binding has incorrect typing, please either ask the action owner to onboard
   [github-action-typing](https://github.com/typesafegithub/github-actions-typing/), or if it's not possible, contribute
   to [github-actions-typing-catalog](https://github.com/typesafegithub/github-actions-typing-catalog).
1. The bindings server currently caches the artifacts for a given owner/name/version tuple for 1 hour
   ([code](https://github.com/typesafegithub/github-workflows-kt/blob/1b9a7bf03982a33fc82cbc57cb41cb6ebd4c1f1b/jit-binding-server/src/main/kotlin/io/github/typesafegithub/workflows/jitbindingserver/Main.kt#L26)).
   If you think it's too much/too little, or a different caching strategy would be better, please reach out via GitHub
   issues!
1. Regarding dependency update bots, Renovate is able to put updates to the Kotlin script and the YAML in a single PR,
   like [here](https://github.com/LeoColman/Petals/pull/510/files). It wasn't tested with Dependabot yet, feel free to
   give it a try!
