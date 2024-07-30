# Using actions

As a reminder, to be able to use an action, you have to know its owner, name and version, e.g. `actions/checkout@v3`.
You can use any action you want. Read on to learn about your options.

## Maven-compatible action bindings repository

!!! info "When to use this approach"
    This is the recommended, default approach. Start with this.

To add a dependency on an action:
1. Add a dependency on a Maven repository that generates the action bindings on the fly: https://bindings.krzeminski.it/
2. Add a dependency on a Maven artifact, e.g. for `actions/checkout@v3` the right way to add the dependency in the
   script is: `@file:DependsOn("actions:checkout:v3")`. As you can see, the group ID was adopted to model the action's
   owner, the artifact ID models the action name, and the version is just action's version (a tag or a branch
   corresponding to a released version). If an action's manifest is defined in a subdirectory, like the popular
   `gradle/actions/setup-gradle@v3`, replace the slashes in the action name with `__`, so in this case it would be
   `@file:DependsOn("gradle:actions__setup-gradle:v3")`.
3. Use the action by importing a class like `io.github.typesafegithub.workflows.actions.actions.Checkout`.

For every action, a binding will be generated. However, some less popular actions don't have typings configured for
their inputs, so by default all inputs are of type `String`. There are two ways of configuring typings:
1. Recommended: a typing manifest (`action-typing.yml`) in the action's repo, see
   [github-actions-typing](https://github.com/typesafegithub/github-actions-typing/). Thanks to this, the actions' owner
   is responsible for providing and maintaining the typings defined in a technology-agnostic way, to be used
   **not only** with this Kotlin library. There are also no synchronization issues between the action itself and its
   typings. When trying to use a new action that has no typings, always discuss this approach with the action owner
   first.
2. Fallback: if it's not possible to host the typings with the action, use
   [github-actions-typing-catalog](https://github.com/typesafegithub/github-actions-typing-catalog). You can contribute
   or fix typings for your favorite action.

This approach supports dependency updating bots that support Kotlin Script's `.main.kts` files. E.g. Renovate is known
to support it.

## User-defined actions

If you are in a hurry and adding typings is not possible right now, browse these options.

### Typed binding

!!! info "When to use this approach"
    It lets you create an action binding in a similar manner that is provided by the action bindings server i.e. a class
    that takes some constructor arguments with types of your choice, and maps them to strings inside `toYamlArguments`.
    Use it to have better type-safety when using the binding.

#### Repository based actions

In case of a repository based action which most GitHub actions are, inherit from [`RegularAction`](https://github.com/typesafegithub/github-workflows-kt/blob/main/github-workflows-kt/src/main/kotlin/io/github/typesafegithub/workflows/domain/actions/RegularAction.kt)
and in case of actions without explicit outputs, use the `Actions.Outputs` class as type argument:

```kotlin
--8<-- "UsingActionsSnippets.kt:action-without-outputs"
```

or, in case of actions with explicit outputs, create a subclass of `Action.Outputs` for the type argument:

```kotlin
--8<-- "UsingActionsSnippets.kt:action-with-outputs-1"
--8<-- "UsingActionsSnippets.kt:action-with-outputs-2"
```

Once you've got your action, it's now as simple as using it like this:

```kotlin
--8<-- "UsingActionsSnippets.kt:using"
```

#### Local actions

In case of a local action you have available in your repository or cloned from a private repository,
inherit from [`LocalAction`](https://github.com/typesafegithub/github-workflows-kt/blob/main/github-workflows-kt/src/main/kotlin/io/github/typesafegithub/workflows/domain/actions/LocalAction.kt) instead:

```kotlin
--8<-- "UsingActionsSnippets.kt:local-action"
```

#### Published Docker actions

In case of a published Docker action, inherit from [`DockerAction`](https://github.com/typesafegithub/github-workflows-kt/blob/main/github-workflows-kt/src/main/kotlin/io/github/typesafegithub/workflows/domain/actions/DockerAction.kt) instead:

```kotlin
--8<-- "UsingActionsSnippets.kt:docker-action"
```

### Untyped binding

!!! info "When to use this approach"
    It omits typing entirely, and both inputs and outputs are referenced using strings. Use it if you don't care about
    types because you're in the middle of experimenting. It's also more convenient to produce such code by a code
    generator.

#### Repository based actions

In case of a repository based action which most GitHub actions are, use a [`CustomAction`](https://github.com/typesafegithub/github-workflows-kt/blob/main/github-workflows-kt/src/main/kotlin/io/github/typesafegithub/workflows/actions/CustomAction.kt):

```kotlin
--8<-- "UsingActionsSnippets.kt:custom-action"
```

If your custom action has outputs, you can access them, albeit in a type-unsafe manner:

```kotlin
--8<-- "UsingActionsSnippets.kt:custom-action-outputs"
```

#### Local actions

In case of a local action you have available in your repository or cloned from a private repository,
use a [`CustomLocalAction`](https://github.com/typesafegithub/github-workflows-kt/blob/main/github-workflows-kt/src/main/kotlin/io/github/typesafegithub/workflows/actions/CustomLocalAction.kt) instead:

```kotlin
--8<-- "UsingActionsSnippets.kt:custom-local-action"
```

#### Published Docker actions

In case of a published Docker action, use a [`CustomDockerAction`](https://github.com/typesafegithub/github-workflows-kt/blob/main/github-workflows-kt/src/main/kotlin/io/github/typesafegithub/workflows/actions/CustomDockerAction.kt) instead:

```kotlin
--8<-- "UsingActionsSnippets.kt:custom-docker-action"
```
