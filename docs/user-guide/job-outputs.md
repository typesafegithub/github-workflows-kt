# Job outputs

It's possible to pass output from a job in a somewhat type-safe way (that is: types aren't checked, but the field names
are).

First, define `outputs` parameter in `job` function, inheriting from `JobOutputs`:

```kotlin hl_lines="4-7"
val myJob = job(
    id = "my_job",
    runsOn = RunnerType.UbuntuLatest,
    outputs = object : JobOutputs() {
        var myOutput by output()
        var anotherOutput by output()
    },
) { ... }
```

To set an output from within the job, use `jobOutputs`, and then an appropriate object field:

```kotlin
jobOutputs.myOutput = someStep.outputs.someStepOutput
jobOutputs.anotherOutput = someStep.outputs["custom-output"]
```

and then use job's output from another job this way:

```kotlin hl_lines="9-10"
job(
    id = "use_output",
    runsOn = RunnerType.UbuntuLatest,
    needs = listOf(myJob),
) {
    run(
        name = "Use outputs",
        command = """
            echo ${expr { myJob.outputs.myOutput }}
            echo ${expr { myJob.outputs.anotherOutput }}
        """.trimIndent(),
    )
}
```
