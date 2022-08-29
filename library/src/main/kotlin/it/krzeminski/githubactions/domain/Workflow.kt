package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.domain.triggers.Trigger
import it.krzeminski.githubactions.dsl.HasCustomArguments
import kotlinx.serialization.Contextual
import java.nio.file.Path

data class Workflow(
    val name: String,
    val on: List<Trigger>,
    val env: LinkedHashMap<String, String>,
    val sourceFile: Path,
    val targetFileName: String,
    val concurrency: Concurrency? = null,
    val yamlConsistencyJobCondition: String? = null,
    val jobs: List<Job>,
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : HasCustomArguments
