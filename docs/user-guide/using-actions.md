# Using actions

As a reminder, to be able to use an action, you have to know its owner, name and version, e.g. `actions/checkout@v3`.
The library comes bundled with some popular actions, but you can consume any action you want. Read on to learn about
your possibilities.

## Built-in actions

Take a look here: [Supported actions](../supported-actions.md).
These are actions ready to use, grouped by owners. For `actions/checkout@v3`, there's [`CheckoutV3`](https://github.com/krzema12/github-workflows-kt/blob/main/library/src/gen/kotlin/it/krzeminski/githubactions/actions/actions/CheckoutV3.kt)
accepting all inputs defined in its metadata file, along with some basic typing. You may notice that for each major
version, a separate class exists. It's because it's assumed [Semantic Versioning](https://semver.org/) is used to
version the actions, as [recommended by GitHub](https://docs.github.com/en/actions/creating-actions/about-custom-actions#using-tags-for-release-management).
Each new major version means a breaking change, and it usually means that the Kotlin wrapper for the action needs a
breaking change as well.

### Requirements for adding a new action

An action is eligible to be added to this library (i.e. have its Kotlin wrapper generated and maintained by the library)
if the following conditions are fulfilled:

* follows [Semantic Versioning](https://semver.org/), with exceptions for pre-releases (like `v0.2`)
* provides major version tags, as described [here](https://docs.github.com/en/actions/creating-actions/about-custom-actions#using-tags-for-release-management).
  An example valid tag is `v2` that points to the newest release with major version number 2. Example invalid tags are
  `v2.1.0`, `latest` or `main`

Nice to have:

* provides typings using [github-actions-typing](https://github.com/krzema12/github-actions-typing/)

## User-defined actions

If your action is not bundled with the library, you are in a hurry and contributing to the library now is not an option,
you have two ways to proceed.

### Typed wrapper

!!! info "When to use this approach"
    It lets you create an action wrapper in a similar manner that is provided by the built-in action wrappers in this
    library, i.e. a class that takes some constructor arguments with types of your choice, and maps them to strings
    inside `toYamlArguments`. Use it to have better type-safety when using the wrapper.

Inherit from [`Action`](https://github.com/krzema12/github-workflows-kt/blob/main/library/src/main/kotlin/it/krzeminski/githubactions/actions/Action.kt)
in case of actions without outputs:

```kotlin
class MyCoolActionV3(
    private val someArgument: String,
) : Action("acmecorp", "cool-action", "v3") {
    override fun toYamlArguments() = linkedMapOf(
        "some-argument" to someArgument,
    )
}
```

or, in case actions with outputs, from [`ActionWithOutputs`](https://github.com/krzema12/github-workflows-kt/blob/main/library/src/main/kotlin/it/krzeminski/githubactions/actions/ActionWithOutputs.kt):

```kotlin
class MyCoolActionV3(
    private val someArgument: String,
) : ActionWithOutputs<MyCoolActionV3.Outputs>("acmecorp", "cool-action", "v3") {
    override fun toYamlArguments() = linkedMapOf(
        "some-argument" to someArgument,
    )
    
    override fun buildOutputObject(stepId: String) = Outputs(stepId)
    
    class Outputs(private val stepId: String) {
        public val coolOutput: String = "steps.$stepId.outputs.coolOutput"
        
        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
```

Once you've got your action, it's now as simple as using it like this:

```kotlin
uses(name = "FooBar",
     action = MyCoolActionV3(someArgument = "foobar"))
```

### Untyped wrapper

!!! info "When to use this approach"
    It omits typing entirely, and both inputs and outputs are referenced using strings. Use it if you don't care about
    types because you're in the middle of experimenting. It's also more convenient to produce such code by a code
    generator.

Use a [`CustomAction`](https://github.com/krzema12/github-workflows-kt/blob/main/library/src/main/kotlin/it/krzeminski/githubactions/actions/CustomAction.kt):

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

If your custom action has outputs, you can access them, albeit in a type-unsafe manner:

```kotlin
job("test_job", runsOn = RunnerType.UbuntuLatest) {
    val customActionStep = uses(
        name = "Some step with output",
        action = customAction,
    )
    
    // use your outputs:
    println(expr(customActionStep.outputs["custom-output"]))
}
```
