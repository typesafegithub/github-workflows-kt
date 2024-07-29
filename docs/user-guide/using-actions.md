TODO: ADJUST IT!

# Using actions

As a reminder, to be able to use an action, you have to know its owner, name and version, e.g. `actions/checkout@v3`.
The library comes bundled with some popular actions, but you can consume any action you want. Read on to learn about
your possibilities.

## Built-in actions

Take a look here: [Supported actions](../supported-actions.md).
These are actions ready to use, grouped by owners. For `actions/checkout@v3`, there's [`CheckoutV3`](https://github.com/typesafegithub/github-workflows-kt/blob/main/github-workflows-kt/src/gen/kotlin/io/github/typesafegithub/workflows/actions/actions/CheckoutV3.kt)
accepting all inputs defined in its metadata file, along with some basic typing. You may notice that for each major
version, a separate class exists. It's because it's assumed [Semantic Versioning](https://semver.org/) is used to
version the actions, as [recommended by GitHub](https://docs.github.com/en/actions/creating-actions/about-custom-actions#using-tags-for-release-management).
Each new major version means a breaking change, and it usually means that the Kotlin binding for the action needs a
breaking change as well.

### Requirements for adding a new action

An action is eligible to be added to this library (i.e. have its Kotlin binding generated and maintained by the library)
if the following conditions are fulfilled:

* follows [Semantic Versioning](https://semver.org/), with exceptions for pre-releases (like `v0.2`)
* provides major version tags, as described [here](https://docs.github.com/en/actions/creating-actions/about-custom-actions#using-tags-for-release-management).
  An example valid tag is `v2` that points to the newest release with major version number 2. Example invalid tags are
  `v2.1.0`, `latest` or `main`

Nice to have:

* provides typings using [github-actions-typing](https://github.com/typesafegithub/github-actions-typing/)

## User-defined actions

If your action is not bundled with the library, you are in a hurry and contributing to the library now is not an option,
you have two ways to proceed.

### Typed binding

!!! info "When to use this approach"
    It lets you create an action binding in a similar manner that is provided by the built-in action bindings in this
    library, i.e. a class that takes some constructor arguments with types of your choice, and maps them to strings
    inside `toYamlArguments`. Use it to have better type-safety when using the binding.

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
