package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.dsl.CustomValue
import it.krzeminski.githubactions.dsl.HasCustomArguments
import java.nio.file.Path

data class Workflow(
    val name: String,
    val on: List<Trigger>,
    val env: LinkedHashMap<String, String>,
    val sourceFile: Path,
    val targetFile: Path,
    val jobs: List<Job>,
) : HasCustomArguments {
    override val _customArguments: Map<String, CustomValue> =
        mutableListOf()
}
