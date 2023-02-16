# Job outputs

It's possible to pass output from a job in a somewhat type-safe way (that is: types aren't checked, but the field names
are).

First, define `outputs` parameter in `job` function, inheriting from `JobOutputs`:

```kotlin hl_lines="4-7"
--8<-- "JobOutputsSnippets.kt:defineJobOutputs1"
--8<-- "JobOutputsSnippets.kt:defineJobOutputs2"
```

To set an output from within the job, use `jobOutputs`, and then an appropriate object field:

```kotlin
--8<-- "JobOutputsSnippets.kt:setJobOutputs"
```

and then use job's output from another job this way:

```kotlin hl_lines="9-10"
--8<-- "JobOutputsSnippets.kt:useJobOutputs"
```
