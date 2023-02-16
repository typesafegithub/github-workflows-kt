package it.krzeminski.githubactions.docsnippets

import io.kotest.core.spec.style.FunSpec
import it.krzeminski.githubactions.actions.actions.UploadArtifactV3
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.workflow

class CompensatingLibrarysMissingFeaturesSnippets : FunSpec({
    test("customArguments") {
/* ktlint-disable indent */
// --8<-- [start:customArguments1]
workflow(
// --8<-- [end:customArguments1]
    name = "customArguments",
    on = listOf(Push()),
// --8<-- [start:customArguments2]
    // ...
    _customArguments = mapOf(
        "dry-run" to true,
        "some-string-value" to "foobar",
        "written-by" to listOf("Alice", "Bob"),
        "concurrency" to mapOf(
            "group" to expr("github.ref"),
            "cancel-in-progress" to "true",
        ),
    ),
)
// --8<-- [end:customArguments2]
{
    job(id = "test_job", runsOn = UbuntuLatest) {
        run(command = "echo 'Hello world!'")
    }
}
/* ktlint-enable indent */
    }

    test("customInputs") {
/* ktlint-disable indent */
// --8<-- [start:customInputs1]
UploadArtifactV3(
// --8<-- [end:customInputs1]
    path = emptyList(),
// --8<-- [start:customInputs2]
    // ...
    _customInputs = mapOf(
        "path" to "override-path-value",
        "answer" to "42",
    ),
)
// --8<-- [end:customInputs2]
/* ktlint-enable indent */
    }

    test("customVersion") {
/* ktlint-disable indent */
// --8<-- [start:customVersion1]
UploadArtifactV3(
// --8<-- [end:customVersion1]
    path = emptyList(),
// --8<-- [start:customVersion2]
    // ...
    _customVersion = "v4",
)
// --8<-- [end:customVersion2]
/* ktlint-enable indent */
    }
},)
