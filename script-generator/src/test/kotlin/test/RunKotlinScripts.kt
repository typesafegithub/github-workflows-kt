package test

import generated.workflowAllTriggers
import generated.workflowDockerImage
import generated.workflowGenerateWrappers
import generated.workflowGenerated
import generated.workflowNodejsPackage
import generated.workflowRefreshversionsBuild
import generated.workflowRefreshversionsPr
import generated.workflowRefreshversionsWebsite
import generated.workflowUpdateGradleWrapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
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
        workflowDockerImage,
    )

    test("Execute Kotlin Scripts") {
        val gitRootDir = tempdir().also {
            it.resolve(".git").mkdirs()
        }.toPath()
        allWorkflows.forEach {
            it.copy(sourceFile = gitRootDir.resolve(it.sourceFile))
                .writeToFile(addConsistencyCheck = false)
        }
    }
},)
