# Module's purpose

This module is responsible for reading metadata about actions for which Kotlin bindings should be generated.

It uses data stored in `actions` root directory and produces a collection of Kotlin objects expressing actions supported
by the library. This collection is available as a `bindingsToGenerate` list in
[BindingsToGenerate.kt](src/main/kotlin/io/github/typesafegithub/workflows/actionsmetadata/BindingsToGenerate.kt).
