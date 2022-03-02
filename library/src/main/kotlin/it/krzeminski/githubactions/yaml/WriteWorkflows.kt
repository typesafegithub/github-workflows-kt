package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.domain.Workflow
import kotlin.io.path.writeText

fun writeWorkflowsOrPrintYaml(
    addConsistencyCheck: Boolean = true,
    workflows: List<Workflow>,
    vararg args: String,
) {
    val map = workflows.associateBy { it.name }
    if (args.isNotEmpty()) {
        val workflow = map[args[0]]
        if (workflow != null) {
            println(workflow.toYaml(addConsistencyCheck = addConsistencyCheck))
        } else {
            throw IllegalArgumentException("unknown workflow name '${args[0]}'")
        }
    } else {
        map.forEach { (name, workflow) ->
            println("writing $name")
            val yaml = workflow.toYaml(addConsistencyCheck = addConsistencyCheck) + System.lineSeparator()
            workflow.targetFile.writeText(yaml)
        }
    }
}
