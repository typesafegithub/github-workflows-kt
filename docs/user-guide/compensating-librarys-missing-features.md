# Compensating library's missing features

You may find yourself willing to use GitHub Actions' feature that is not yet reflected in this library, neither in the
core workflows/jobs/steps API, nor in the action wrappers. We've thought about it. The library provides several points
of extension so that you can keep using it, and in the meantime report the missing feature to us so that we can add it
to one of the next releases. See the below sections to find your specific case.

The general approach is that whatever is overridden/customized using the below approaches, takes the precedence over
built-in arguments.

## Workflows, jobs and steps

They have an extra argument - `_customArguments` - which is a map from `String` to whatever values or collections are
needed, especially using basic types like booleans, strings or integers, and further nesting of maps and lists.

For example:

```kotlin
workflow(
    //...
    _customArguments = mapOf(
        "dry-run" to true,
        "some-string-value" to "foobar",
        "written-by" to listOf("Alice", "Bob"),
        "concurrency" to mapOf(
            "group" to expr("github.ref"),
            "cancel-in-progress" to "true",
        )
    ),
)
```

## Action's inputs

Each action wrapper has an extra constructor parameter - `_customInputs` - which is a map from `String` to `String`:

```kotlin
UploadArtifactV3(
    //...
    _customInputs = mapOf(
        "path" to "override-path-value",
        "answer" to "42",
    )
)
```

You can use it to set inputs that the wrapper doesn't know about, or to set any custom value if the wrapper's typing is
incorrect or faulty.

## Action's version

Each action wrapper has an extra constructor parameter - `_customVersion` - which is a string overriding action's
version:

```kotlin
UploadArtifactV3(
    //...
    _customVersion = "v4"
)
```

It's useful e.g. when the wrapper doesn't keep up with action's versions and the API is fairly compatible, or if you
want to use a specific minor version.

## I still cannot customize what I need

Well, it means we missed something - sorry for that! Please report it via GitHub issues.
