package test

import generated.workflowAllTriggers
import generated.workflowCheckBuild
import generated.workflowGenerateWrappers
import generated.workflowGenerated
import generated.workflowPublishMkdocs
import generated.workflowRefreshversionsPr
import generated.workflowUpdateGradleWrapper
import io.kotest.core.spec.style.FunSpec
import it.krzeminski.githubactions.yaml.writeToFile

class RunKotlinScripts: FunSpec({
    val allWorkflows = listOf(
        workflowGenerateWrappers,
        workflowCheckBuild,
        workflowAllTriggers,
        workflowGenerated,
        workflowRefreshversionsPr,
        workflowPublishMkdocs,
        workflowUpdateGradleWrapper
    )


    test("Execute Kotlin Scripts") {
        allWorkflows.forEach { it.writeToFile(addConsistencyCheck = false) }
    }
})
