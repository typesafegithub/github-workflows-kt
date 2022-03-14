package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.dsl.CustomArguments
import it.krzeminski.githubactions.dsl.HasCustomArguments
import java.nio.file.Path

data class Workflow(
    val name: String,
    val on: List<Trigger>,
    val env: LinkedHashMap<String, String>,
    val sourceFile: Path,
    val targetFile: Path,
    val jobs: List<Job>,
    override val _customArguments: CustomArguments = mapOf()
) : HasCustomArguments
