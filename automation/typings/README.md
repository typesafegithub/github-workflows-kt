# Module's purpose

This module is responsible for reading metadata about actions for which Kotlin wrappers should be generated.

It uses data stored in `actions` root directory and produces a collection of Kotlin objects expressing actions supported
by the library. This collection is available as a `wrappersToGenerate` list in
[WrappersToGenerate.kt](src/main/kotlin/it/krzeminski/githubactions/actionsmetadata/WrappersToGenerate.kt).
