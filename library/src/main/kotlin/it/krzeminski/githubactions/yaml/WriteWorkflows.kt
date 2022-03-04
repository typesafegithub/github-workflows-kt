package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.domain.Workflow

fun writeWorkflows(
    addConsistencyCheck: Boolean = true,
    workflows: List<Workflow>,
) {
    workflows.forEach {
        it.writeToFile(addConsistencyCheck)
    }
}

fun writeWorkflows(
    addConsistencyCheck: Boolean = true,
    vararg workflows: Workflow,
) {
    writeWorkflows(
        addConsistencyCheck,
        listOf(*workflows)
    )
}
