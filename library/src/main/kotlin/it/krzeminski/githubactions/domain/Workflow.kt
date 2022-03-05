package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.dsl.FreeYamlArgs
import it.krzeminski.githubactions.dsl.HasFreeYamlArgs
import java.nio.file.Path

data class Workflow(
    val name: String,
    val on: List<Trigger>,
    val env: LinkedHashMap<String, String>,
    val sourceFile: Path,
    val targetFile: Path,
    val jobs: List<Job>,
) : HasFreeYamlArgs {
    override val freeYamlArgs: FreeYamlArgs = mutableListOf()
}
