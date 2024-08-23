# Job outputs

It is possible to pass output from a job in a type-safe way.

First, define `outputs` parameter in `job` function, inheriting from `JobOutputs` with properly typed output properties:

```kotlin
--8<-- "JobOutputsSnippets.kt:define-job-outputs-1"
--8<-- "JobOutputsSnippets.kt:define-job-outputs-2"
```

To set an output from within the job, use `jobOutputs`, and then an appropriate object field:

```kotlin
--8<-- "JobOutputsSnippets.kt:set-job-outputs"
```

and then use job's output from another job this way:

```kotlin
--8<-- "JobOutputsSnippets.kt:use-job-outputs"
```
