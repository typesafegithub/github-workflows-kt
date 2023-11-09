# Migrating to client-side bindings generation

## Goal

Let's say you have the following workflow:

```kotlin
#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:<newest-version>")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Build",
    on = listOf(PullRequest()),
    sourceFile = __FILE__.toPath(),
) {
    job(id = "build", runsOn = UbuntuLatest) {
        uses(action = CheckoutV4())
        uses(action = SetupJavaV3())
        uses(
            name = "Build",
            action = GradleBuildActionV2(
                arguments = "build",
            )
        )
    }
}.writeToFile()
```

`CheckoutV4`, `SetupJavaV3` and `GradleBuildActionV2` come with the library - they are bundled together with the DSL.
We're going to stop using the bundled classes in favor of generating the bindings on demand.

## Steps

1. Create a new file under `.github/workflows/_used_actions.yaml` with the following contents:
   ```yaml
   # This workflow isn't meant to be run. It exists solely for the purpose of using it with
   # https://github.com/typesafegithub/github-workflows-kt. Thanks to defining used actions this way, the library can
   # generate type-safe bindings, and dependency updating bots like Renovate or Dependabot can update actions' versions.

   on:
     push:
       branches: [this-branch-must-never-be-created]

   jobs:
     some-job:
       runs-on: ubuntu-latest
       steps:
         # List used actions below, as subsequent "- uses" items:
         - uses: actions/checkout@v4
         - uses: actions/setup-java@v3
         - uses: gradle/gradle-build-action@v2
   ```
2. Create another file, under `.github/workflows/generate-action-bindings.main.kts`, with this contents:
   ```kotlin
   #!/usr/bin/env kotlin
   @file:DependsOn("io.github.typesafegithub:action-binding-generator:<newest-version>")

   import io.github.typesafegithub.workflows.actionbindinggenerator.generateActionBindings

   generateActionBindings(args)
   ```
   and make this file executable (`chmod +x <path-to-file>` on Linux/Unix).  
   This file has two roles:
    * it will be called in the consistency check job, prior to actually checking the consistency, to generate the
      bindings
    * you can call it locally to get the bindings for workflow development
3. Call `.github/workflows/generate-action-bindings.main.kts` to generate the bindings.
4. Add `.github/workflows/generated` to .gitignore.
5. Replace bundled binding classes with the generated ones, together with opting into using client-side binding
   generation. For the example workflow, you need to make these changes:
   ```diff
     #!/usr/bin/env kotlin
     @file:DependsOn("io.github.typesafegithub:github-workflows-kt:<newest-version>")
   + @file:Import("generated/actions/checkout.kt")
   + @file:Import("generated/actions/setup-java.kt")
   + @file:Import("generated/gradle/gradle-build-action.kt")

   - import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
   - import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
   - import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
     import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
     import io.github.typesafegithub.workflows.domain.triggers.PullRequest
     import io.github.typesafegithub.workflows.dsl.workflow
     import io.github.typesafegithub.workflows.yaml.writeToFile
  
     workflow(
         name = "Build",
         on = listOf(PullRequest()),
         sourceFile = __FILE__.toPath(),
     ) {
         job(id = "build", runsOn = UbuntuLatest) {
   -         uses(action = CheckoutV4())
   +         uses(action = Checkout())
   -         uses(action = SetupJavaV3())
   +         uses(action = SetupJava())
             uses(
                 name = "Build",
   -             action = GradleBuildActionV2(
   +             action = GradleBuildAction(
                     arguments = "build",
                 )
             )
         }
   - }.writeToFile()
   + }.writeToFile(generateActionBindings = true)
   ```
6. Regenerate your YAML. You should see an extra step in the consistency check job.

Congratulations, your workflow now uses the client-side bindings generation!
