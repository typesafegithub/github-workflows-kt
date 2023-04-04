# Typesafe GitHub Expressions

## GitHub expressions

GitHub supports pretty advanced expressions via the `${{ ... }}` syntax.

They include:

- functions
- environment variables
- secrets
- different contexts like the `runner` or the `github` context
- events payloads
- and more (read [here](https://docs.github.com/en/actions/learn-github-actions/expressions)).

Here is an example

```kotlin
--8<-- "TypeSafeExpressionsSnippets.kt:ill-example"
```

Unfortunately, it is easy to get those expressions wrong.

In fact this snippet contains _four different errors_.

Can you spot them all?

To make life easier, let us introduce type-safe GitHub expressions.


## The `expr("")` helper function

First, because `\${{ ... }}` is awkward in Kotlin, it can be replaced by the `expr("")` helper function

```diff
- "\${{invariably()}}"
+ expr("invariably()")
```

But this is still not type-safe.


## Type-safe functions with the `expr { }`  DSL

We went one step further towards type-safety by introducing the `expr { }` DSL.

Goals:

- an invalid expression should not even compile.
- increase discoverability of what is available.

For example, you can use auto-completion to find out which functions are available:

<img width="581" src="https://user-images.githubusercontent.com/459464/176900956-74370289-79c4-488f-86a9-afb0c3ba1ba8.png">

Here we immediately see how to fix a first bug in our original snippet:

```diff
- "\${{invariably()}}"
- expr("invariably()")
+ expr { always() }
```

Reference: https://docs.github.com/en/actions/learn-github-actions/expressions#functions

## The `runner` context

The `runner` context contains information about the runner that is executing the current job.

The possible properties are available via `expr { runner.xxx }`
<img width="888" src="https://user-images.githubusercontent.com/459464/176903756-52c102ce-aa60-4d36-865d-1b7545b05ddf.png">

https://docs.github.com/en/actions/learn-github-actions/contexts#example-contents-of-the-runner-context

## The `github` context

The `github` context contains information about the workflow run and the event that triggered the run.

The possible properties are available via `expr { github.xxx }`

<img width="857" src="https://user-images.githubusercontent.com/459464/176904326-4a7685cb-96d6-4ff2-a9b0-7a63f8bdb714.png">
Here we detect immediatly another bug in our original snippet

```diff
-command = "echo commit: ${'$'}{{ github.sha256 }}
+command = "echo commit: " + expr { github.sha }
```

Reference: https://docs.github.com/en/actions/learn-github-actions/contexts#github-context

## The `github.eventXXX` payload

The `github.event` field is special because it depends on what kind of events triggered the workflow:

- Push
- PullRequest
- WorkflowDispatch
- Release
- ...

Since they have a different type, there is a diferent property `expr { github.eventXXX }`  per type:
<img width="893" src="https://user-images.githubusercontent.com/459464/176905276-a3dcfe23-0df9-4ba5-bf6a-bd519df4466e.png">

By leveraging this feature, we quickly fix another bug in our original snippet:

<img width="995" src="https://user-images.githubusercontent.com/459464/176904991-12eb7d1e-cafe-4080-a668-608d4b682451.png">

## Default environment variables
GitHub supports a number of default environment variables.

They are available directly in the IDE via the library's `Contexts.env`
<img width="873" src="https://user-images.githubusercontent.com/459464/176902143-454254d3-2bb7-4b93-91dd-64a589d980a8.png">
By using this feature in our snippet we would have avoided escaping the dollar and the typo:

```diff
-command = "echo \$GITHUB_ACTORS",
+command = "echo " + Contexts.env.GITHUB_ACTOR,
```

Reference: https://docs.github.com/en/actions/learn-github-actions/environment-variables#default-environment-variables

## Custom environment variables

You are not limited to the default environment variables.

You can create your own type-safe property by using the syntax

> `val MY_VARIABLE_NAME by Contexts.env`

For example:

```kotlin
--8<-- "TypeSafeExpressionsSnippets.kt:custom-environment-variables-1"
--8<-- "TypeSafeExpressionsSnippets.kt:custom-environment-variables-2"
```

Reference: https://docs.github.com/en/actions/learn-github-actions/environment-variables#about-environment-variables

## GitHub Secrets

If you have sensitive information, you should store it as a GitHub secret:

<img width="1372" alt="Actions_secrets" src="https://user-images.githubusercontent.com/459464/176906278-9262973f-885e-4b5b-9d29-f24acad8302b.png">

You use them the same way as environment variables, but using `Contexts.secrets` instead of `Contexts.env`:

> `val SUPER_SECRET by Contexts.secrets`

For example:

```kotlin
--8<-- "TypeSafeExpressionsSnippets.kt:secrets"
```


## Missing a feature?

GitHub has more contexts that we don't support yet: https://docs.github.com/en/actions/learn-github-actions/contexts

There are more `github.event` payloads that we currently do not support: https://docs.github.com/en/developers/webhooks-and-events/webhooks/webhook-events-and-payloads

We feel what we have is a pretty good start, but if you need an additional feature, you can [create an issue](https://github.com/typesafegithub/github-workflows-kt/issues)

Or maybe have a look how this type-safe feature is implemented in [io.github.typesafegithub.workflows.dsl.expressions](https://github.com/typesafegithub/github-workflows-kt/tree/main/library/src/main/kotlin/io/github/typesafegithub/workflows/dsl/expressions) and [submit a pull request 🙏🏻](https://github.com/typesafegithub/github-workflows-kt/pulls)
