package test

import generated.workflowAllTriggers
import generated.workflowGenerateWrappers
import generated.workflowGenerated
import generated.workflowNodejsPackage
import generated.workflowRefreshversionsBuild
import generated.workflowRefreshversionsPr
import generated.workflowRefreshversionsWebsite
import generated.workflowUpdateGradleWrapper
import io.kotest.core.spec.style.FunSpec
import it.krzeminski.githubactions.yaml.writeToFile

class RunKotlinScripts : FunSpec({
    val allWorkflows = listOf(
        workflowGenerateWrappers,
        workflowRefreshversionsBuild,
        workflowAllTriggers,
        workflowGenerated,
        workflowRefreshversionsPr,
        workflowRefreshversionsWebsite,
        workflowUpdateGradleWrapper,
        workflowNodejsPackage,
    )

    test("Execute Kotlin Scripts") {
        allWorkflows.forEach { it.writeToFile(addConsistencyCheck = false) }
    }
})
