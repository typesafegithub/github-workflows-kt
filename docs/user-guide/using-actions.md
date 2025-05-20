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

    ??? tip "Dealing with stale Maven cache, a.k.a. using version ranges"
        Additionally, the name part can have the suffix `___major` or `___minor` (three leading underscores).
        Without these suffixes if you request a version `v1.2.3`, the generated YAML will also use exactly
        `v1.2.3` unless you use a custom version override. With the `___major` suffix, it would only write `v1`
        to the generated YAML, with the `___minor` suffix - `v1.2`.

        This is especially useful when combined with a version range. The problem with using `v1` or `v1.2` is that for
        GitHub Actions, these are changing tags or changing branches and not static releases. In the Maven world,
        however, a version that does not end with `-SNAPSHOT` is considered immutable and is not expected to change.
        This means that if a new version of the action is released that adds a new input, you cannot use it easily
        as you still have the old `v1` artifact in your Maven cache and it will not be updated usually,
        even though the binding server provides a new binding including the added input. And even if you remove the
        old version from the Maven cache and get a new version from the bindings server, other people might also have
        this outdated version in their Maven cache and then fail compilation with your changes.
        It's worth emphasizing that this problem is currently present only when iterating
        on your workflows locally. When running on GitHub Actions, this problem doesn't
        exist because the state of Maven Local repo isn't cached between the runs.

        To mitigate this problem, you can for example use a dependency like
        `gradle:actions__setup-gradle___major:[v3,v4)` (from `v3` inclusive to `v4` exclusive).
        This will resolve to the latest `v3.x.y` version and thus include any newly added inputs,
        but still only write `v3` to the YAML. Without the `___major` suffix or a not semantically matching
        range like `[v3,v5)` or even `[v3,v4]`, you will get problems with the consistency check as
        then the YAML output changes as soon as a new version is released. For a minor version
        you would accordingly use the `___minor` suffix together with a range like `[v4.0,v4.1)` to get
        the latest `v4.0` release if the action in question provides such a tag or branch.

        !!! info
            If an action maintainer provides pre-releases that follow certain naming conventions as documented in the
            [Maven Documentation](https://maven.apache.org/pom.html#Version_Order_Specification), you might need to
            adjust the upper bound. For exmple a version `v4.0-beta` is less than `v4` and thus part of the range
            `[v3,v4)`. In such a case - or always, to be on the safe side - you might want to change the range to
            `[v3,v4-alpha)`, as the `alpha` version is the lowest possible version in Maven semantics.

3. Use the action by importing a class like `io.github.typesafegithub.workflows.actions.actions.Checkout`.

For every action, a binding will be generated. However, some less popular actions don't have typings configured for
their inputs, so by default all inputs are of type `String`, have the suffix `_Untyped`, and additionally the class
name will have an `_Untyped` suffix.

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
there will still be only the property with `_Untyped` suffix. For each input that does have type information available,
there will still be the `_Untyped` property and additionally a properly typed property. It is a runtime error to set
both of these properties as well as setting none if the input is required.

All inputs also get another `Expression` suffixed property and list-typed inputs yet another `Expressions` suffixed
property. These properties can e.g. be set type-safely from outputs, as they are of type `Expression`. Each output also
has an `_Untyped` suffixed property that you can feed to any input expression property. Like with the `_Untyped` input
property, all up to four properties for the same input are mutually exclusive and for required inputs exactly one has
to be set, or a runtime error occurs.

This approach supports dependency updating bots that support Kotlin Script's `.main.kts` files. E.g. Renovate is known
to support it.

### Binary compatibility of generated bindings

The generated bindings are not guaranteed to be binary compatible. As soon as an action owner for example adds a
new input, the signature of the constructor changes and the signature of the `copy` method of that action's binding
changes. So you should not use these generated bindings in any pre-compiled code, unless you are pretty sure you
know what you are doing and are aware of the consequences.

The typical usage of the generated bindings are not affected by this. By technically enforcing the usage of named
arguments when using the constructor or `copy` method, the bindings stay source compatible even if new inputs are
added. So if you are just using the bindings within `.main.kts` scripts for which they are designed, you are safe.
The only exception is, when an action owner removes or renames an input. But according to semver, this is a breaking
change and an action author should not do this within the same major version. If he nevertheless did, open a bug
issue in that action's GitHub repository so the owner can revert the change and instead release a new major version.

### Available repository URL

Evolving this library and / or the generated bindings sometimes requires breaking changes in the generated bindings.
To not break your existing workflow scripts, the bindings delivered on a given repository URL should always stay
backwards compatible (in terms of source compatibility). If accidentally a breaking change was done on given repository
URL, please open a bug report. Unless very good reasons object, the change will be reverted and instead be done on
a different repository URL. Such a situation is usually indicated by your workflow scripts suddenly failing to execute
without you having done any changes to it, except if the breaking change is caused by an action author doing a breaking
change inappropriately. In that case, please open a bug report with that action's GitHub repository instead.

#### Stability statuses

*Experimental*

:   If a new repository URL is added, it will initially be in experimental state. When using such a URL, you get the
    cutting edge changes, but no stability is guaranteed. Any breaking change can happen at any time, so use these URL
    only if you have that in mind and consent to the consequences. Executing a workflow script with such a repository
    URL will issue a warning and executing it on GitHub Actions will add a warning annotation to the workflow run.

*Stable*

:   Repository URL that are in stable state will not have any source incompatible changes done. There will typically
    only be one stable URL, and that is what you should usually use in your workflow scripts.

*Deprecated*

:   Upon new stable URL becoming available, existing stable URL will most probably be moved to deprecated state.
    Deprecated state for now does not mean, that the repository URL will be turned off. On a deprecated URL also no
    source incompatible changes will be done, but new changes might not be added to the deprecated URL. The main
    intention of deprecating a URL is to hint at newer stable URL being available. For this all bindings generated
    on this URL will be marked as `@Deprecated` and executing the workflow script will issue a warning and if run
    on GitHub Actions will add a warning annotation to the workflow run.

| Repository URL                      | Stability                                                              |
|-------------------------------------|------------------------------------------------------------------------|
| `https://bindings.krzeminski.it`    | :material-check-bold:{ style="color: green" } stable (alias for `/v1`) |
| `https://bindings.krzeminski.it/v1` | :material-check-bold:{ style="color: green" } stable                   |
| `https://bindings.krzeminski.it/v2` | :no_entry: experimental                                                |

#### Library version compatibility

As the library and / or generated bindings evolve, not all library versions will be compatible with all repository URL.
This table lists which library versions are compatible with which repository URL.

| Repository URL                      | Compatibility     |
|-------------------------------------|-------------------|
| `https://bindings.krzeminski.it`    | `3.0.0` and newer |
| `https://bindings.krzeminski.it/v1` | `3.0.0` and newer |
| `https://bindings.krzeminski.it/v2` | `4.0.0` and newer |

#### Breaking changes

This section shows which source incompatible breaking changes were done in each repository URL. When changing
from a previous URL to a later one, these might be issues you are hitting and need to adapt to in your workflow
scripts. Binary breaking changes are not listed here, as binary compatibility is not guaranteed
anyway as defined above.

`https://bindings.krzeminski.it/v2`

:   - Outputs of actions are no longer plain `String`s with the GitHub expression. Instead they are of type
      `Expression<*>` with the type parameter set according to the action typing definition. Those expressions can be
      wired directly to the new `Expression` suffixed sibling properties each input property has now. In case you need
      to wire non-matching types, you can use the `_Untyped` property for the output or get it by name. Those return
      `Expression<Any>` which can be given to all types. You can also still get the plain expression from that
      object to use it within a bigger expression.

`https://bindings.krzeminski.it` / `https://bindings.krzeminski.it/v1`

:   This is the original `v1` URL with which generated bindings have started.

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
