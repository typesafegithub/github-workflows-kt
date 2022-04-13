package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Trigger
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

@GithubActionsDsl
@Suppress("LongParameterList", "FunctionParameterNaming", "ConstructorParameterNaming")
class WorkflowBuilder(
    name: String,
    on: List<Trigger>,
    env: LinkedHashMap<String, String> = linkedMapOf(),
    sourceFile: Path,
    targetFile: Path,
    rootDir: Path = Paths.get("."),
    jobs: List<Job> = emptyList(),
    _customArguments: Map<String, CustomValue>,
) {
    internal var workflow = Workflow(
        name = name,
        on = on,
        env = env,
        sourceFile = sourceFile,
        targetFile = targetFile,
        rootDir = rootDir,
        jobs = jobs,
        _customArguments = _customArguments,
    )

    @Suppress("LongParameterList")
    fun job(
        id: String,
        name: String? = null,
        runsOn: RunnerType,
        needs: List<Job> = emptyList(),
        condition: String? = null,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        strategyMatrix: Map<String, List<String>>? = null,
        _customArguments: Map<String, CustomValue> = mapOf(),
        timeoutMinutes: Int? = null,
        block: JobBuilder.() -> Unit,
    ): Job {
        val jobBuilder = JobBuilder(
            id = id,
            name = name,
            runsOn = runsOn,
            needs = needs,
            condition = condition,
            env = env,
            strategyMatrix = strategyMatrix,
            timeoutMinutes = timeoutMinutes,
            _customArguments = _customArguments,
        )
        jobBuilder.block()
        val newJob = jobBuilder.build()

        require(newJob.steps.isNotEmpty()) {
            "There are no steps defined!"
        }

        workflow = workflow.copy(jobs = workflow.jobs + newJob)
        return newJob
    }

    fun build() = workflow
}

fun Workflow.toBuilder() =
    WorkflowBuilder(
        name = name,
        on = on,
        sourceFile = sourceFile,
        targetFile = targetFile,
        jobs = jobs,
        _customArguments = _customArguments,
    )

@Suppress("LongParameterList", "FunctionParameterNaming")
fun workflow(
    name: String,
    on: List<Trigger>,
    env: LinkedHashMap<String, String> = linkedMapOf(),
    sourceFile: Path,
    targetFile: Path,
    rootDir: Path = Paths.get("."),
    _customArguments: Map<String, CustomValue> = mapOf(),
    block: WorkflowBuilder.() -> Unit,
): Workflow {
    require(on.isNotEmpty()) {
        "There are no triggers defined!"
    }

    val workflowBuilder = WorkflowBuilder(
        name = name,
        on = on,
        env = env,
        sourceFile = sourceFile,
        targetFile = targetFile,
        rootDir = rootDir,
        _customArguments = _customArguments,
    )
    workflowBuilder.block()

    require(workflowBuilder.workflow.jobs.isNotEmpty()) {
        "There are no jobs defined!"
    }
    workflowBuilder.workflow.jobs.requireUniqueJobIds()

    return workflowBuilder.build()
}

@Suppress("LongParameterList", "FunctionParameterNaming")
fun workflow(
    name: String,
    on: List<Trigger>,
    env: LinkedHashMap<String, String> = linkedMapOf(),
    sourceFile: File,
    targetFileName: String = sourceFile.name.substringBeforeLast(".main.kts") + ".yml",
    rootFolder: File = File("."),
    targetFileFolder: File = rootFolder.resolve(".github/workflows"),
    _customArguments: Map<String, CustomValue> = mapOf(),
    block: WorkflowBuilder.() -> Unit,
): Workflow = workflow(
    name = name,
    on = on,
    env = env,
    sourceFile = sourceFile.toPath(),
    targetFile = targetFileFolder.toPath().resolve(targetFileName),
    rootDir = rootFolder.toPath(),
    _customArguments = _customArguments,
    block = block,
)

private fun List<Job>.requireUniqueJobIds() {
    val countPerJobName = this
        .map { it.id }
        .groupBy { it }
        .mapValues { it.value.count() }

    require(countPerJobName.none { it.value > 1 }) {
        val duplicatedJobNames = countPerJobName
            .filter { it.value > 1 }
            .map { it.key }
        "Duplicated job names: $duplicatedJobNames"
    }
}
