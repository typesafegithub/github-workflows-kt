package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.domain.triggers.Trigger
import java.nio.file.Path

data class Workflow(
    val name: String,
    val on: List<Trigger>,
    val env: LinkedHashMap<String, String>,
    val sourceFile: Path,
    val targetFile: Path,
    val jobs: List<Job>,
)
