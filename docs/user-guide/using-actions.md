# Using actions

As a reminder, to be able to use an action, you have to know its owner, name and version, e.g. `actions/checkout@v3`.
The library comes bundled with some popular actions, but you can consume any action you want. Read on to learn about
your possibilities.

## Built-in actions

Take a look here: [library/src/gen/kotlin/it/krzeminski/githubactions/actions](https://github.com/krzema12/github-actions-kotlin-dsl/tree/main/library/src/gen/kotlin/it/krzeminski/githubactions/actions).
These are actions ready to use, grouped by owners. For `actions/checkout@v3`, there's [`CheckoutV3`](https://github.com/krzema12/github-actions-kotlin-dsl/blob/main/library/src/gen/kotlin/it/krzeminski/githubactions/actions/actions/CheckoutV3.kt)
accepting all inputs defined in its metadata file, along with some basic typing. You may notice that for each major
version, a separate class exists. It's because it's assumed [Semantic Versioning](https://semver.org/) is used to
version the actions, as [recommended by GitHub](https://docs.github.com/en/actions/creating-actions/about-custom-actions#using-tags-for-release-management)
(in practice: there are exceptions). Each new major version means a breaking change, and it usually means that the
Kotlin wrapper for the action needs a breaking change as well.

## User-defined actions

If your action is not bundled with the library, you are in a hurry and contributing to the library now is not an option,
you can use a [`CustomAction`](https://github.com/krzema12/github-actions-kotlin-dsl/blob/main/library/src/main/kotlin/it/krzeminski/githubactions/actions/CustomAction.kt)
in case of actions without outputs:

```kotlin
val customAction = CustomAction(
    actionOwner = "xu-cheng",
    actionName = "latex-action",
    actionVersion = "v2",
    inputs = linkedMapOf(
        "root_file" to "report.tex",
        "compiler" to "latexmk",
    )
)
```

If your customAction has outputs, you can access them, albeit in a type-unsafe manner :

```kotlin
job("test_job", RunnerType.UbuntuLatest) {
    val customActionStep = uses(
        name = "Some step with output",
        action = customAction,
    )
    
    // use your outputs:
    println(expr(customActionStep.outputs["custom-output"]))
}
```

## Using actions in jobs

Once you've got your action, it's now as simple as using it like this:

```kotlin
uses(name = "FooBar",
     action = MyCoolActionV3(someArgument = "foobar"))
```
