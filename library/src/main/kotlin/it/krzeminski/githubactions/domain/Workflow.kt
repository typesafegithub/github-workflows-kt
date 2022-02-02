package it.krzeminski.githubactions.domain

import it.krzeminski.githubactions.domain.triggers.Trigger

data class Workflow(
    val name: String,
    val on: List<Trigger>,
    val env: LinkedHashMap<String, String>,
    val sourceFile: String,
    val targetFile: String,
    val jobs: List<Job>,
)
