# Using actions

As a reminder, to be able to use an action, you have to know its owner, name and version, e.g. `actions/checkout@v3`.
You can use any action you want. Read on to learn about your options.

## Maven-compatible action bindings repository

!!! info "When to use this approach"
    This is the recommended, default approach. Start with this.

To add a dependency on an action:
1. If you haven't already, add a dependency on a Maven repository that generates the action bindings on the fly:
   `@file:Repository("https://bindings.krzeminski.it")`.
2. Add a dependency on a Maven artifact, e.g. for `actions/checkout@v3` the right way to add the dependency in the
   script is: `@file:DependsOn("actions:checkout:v3")`. As you can see, the group ID was adopted to model the action's
   owner, the artifact ID models the action name, and the version is just action's version (a tag or a branch
   corresponding to a released version). If an action's manifest is defined in a subdirectory, like the popular
   `gradle/actions/setup-gradle@v3`, replace the slashes in the action name with `__`, so in this case it would be
   `@file:DependsOn("gradle:actions__setup-gradle:v3")`.

   Additionally, the name part can have the suffix `___major` or `___minor`. Without these suffixes if you request
   a version `v1.2.3`, the generated YAML will also use exactly `v1.2.3` unless you use a custom version override.
   with the `___major` suffix, it would only write `v1` to the generated YAML, with the `___minor` suffix `v1.2`.

   This is especially useful when combined with a version range. The problem with using `v1` or `v1.2` is, that for
   GitHub actions these are changing tags or changing branches and not static releases. But in the Maven world
   a version that does not end in `-SNAPSHOT` is considered immutable and is not expected to change. This means that
   if a new version of the action is released that adds a new input, you cannot use it easily as you still have the old
   `v1` artifact in your Maven cache and it will not be updated usually, even though the binding server provides a new
   binding including the added input.

   To mitigate this problem you can for example use a dependency like `gradle/actions/setup-gradle___major@[v3,v4)`.
   This will resolve to the latest `v3.x.y` version and thus include any newly added inputs, but still only write `v3`
   to the YAML. Without the `___major` suffix or a not semantically matching range like `[v3,v5)` or even `[v3,v4]` you
   will get problems with the consistency check as then the YAML output changes as soon as a new version is released.
   For a minor version you would accordingly use the `___minor` suffix together with a range like `[v4.0,v4.1)` to get
   the latest `v4.0` release if the action in question provides such a tag or branch.
3. Use the action by importing a class like `io.github.typesafegithub.workflows.actions.actions.Checkout`.

For every action, a binding will be generated. However, some less popular actions don't have typings configured for
their inputs, so by default all inputs are of type `String`, have the suffix `_Untyped`, and additionally the class
name will have an `_Untyped` suffix. The nullability of the inputs will be according to their required status.

There are two ways of configuring typings:
1. Recommended: a typing manifest (`action-typing.yml`) in the action's repo, see
   [github-actions-typing](https://github.com/typesafegithub/github-actions-typing/). Thanks to this, the action's owner
   is responsible for providing and maintaining the typings defined in a technology-agnostic way, to be used
   **not only** with this Kotlin library. There are also no synchronization issues between the action itself and its
   typings. When trying to use a new action that has no typings, always discuss this approach with the action owner
   first.
2. Fallback: if it's not possible to host the typings with the action, use
   [github-actions-typing-catalog](https://github.com/typesafegithub/github-actions-typing-catalog),
   a community-maintained place to host the typings. You can contribute or fix typings for your favorite action by
   sending a PR.

Once there are any typings in place for the action, the `_Untyped` suffixed class is marked `@Deprecated`, and a class
without that suffix is created additionally. In that class for each input that does not have type information available
there will still be only the property with `_Untyped` suffix and nullability according to required status. For each
input that does have type information available, there will still be the `_Untyped` property and additionally a
properly typed property. Both of these properties will be nullable. It is a runtime error to set both of these
properties as well as setting none if the input is required. The `_Untyped` properties are not marked `@Deprecated`,
as it could still make sense to use them, for example if you want to set the value from a GitHub Actions expression.

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
