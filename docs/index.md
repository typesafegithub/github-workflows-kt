# github-workflows-kt

github-workflows-kt is a tool for generating
[GitHub Actions workflow](https://docs.github.com/en/actions/using-workflows) YAML files in a **type-safe** script, helping you to
build **robust** workflows for your GitHub projects without mistakes, with **pleasure**, in
[Kotlin](https://kotlinlang.org/).

> _You won't go back to YAML!_

## 💡 Idea

We're often surrounded by YAML configuration. It's a powerful format that provides simple syntax for defining
hierarchical data, but it is sometimes used (abused?) to configure complicated scenarios which leads to complicated
files that are difficult to write and maintain.

Who among us hasn't accidentally used the wrong indentation, missed a possibility to extract a reusable piece of code,
or been confused by ambiguous types? The power of a generic-purpose would come in handy in these cases.

We're developing **github-workflows-kt** to solve these and other problems, so you can create GitHub Workflows with
confidence.

## ✨ Benefits

* **no indentation confusion** - Kotlin's syntax doesn't rely on it
* **immediate validation** - catch bugs early during development, not during runtime
* **strongly typed values** - no more confusion about what type is needed for a given parameter
* **superb IDE support** - author your workflows in any IDE that supports Kotlin, with auto-completion and documentation
  at your fingertips
* **no duplication** - don't repeat yourself! Share common configuration using constant values, or define your own
  functions to encapsulate logic
* **fully featured language** - use the full power of Kotlin to generate workflows dynamically, randomly generate data,
  or add custom validation. Defining workflow logic in Kotlin is currently experimental
* **type-safe action bindings** - possible to use every action using auto-generated Kotlin bindings
* integrates with [github-actions-typing](https://github.com/typesafegithub/github-actions-typing) to use typings
  provided by action authors
* and more!

## 🎥 Video presentation

Here's a detailed presentation of the library by Piotr Krzemiński, along with simple demos:

![type:video](https://www.youtube.com/embed/jrDQXqQicek)
