package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.domain.Concurrency
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.JobOutputs
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Trigger
import kotlinx.serialization.Contextual
import java.nio.file.Path

@GithubActionsDsl
@Suppress("LongParameterList", "FunctionParameterNaming", "ConstructorParameterNaming")
public class WorkflowBuilder(
    name: String,
    on: List<Trigger>,
    env: LinkedHashMap<String, String> = linkedMapOf(),
    sourceFile: Path,
    targetFileName: String,
    concurrency: Concurrency? = null,
    yamlConsistencyJobCondition: String? = null,
    jobs: List<Job<*>> = emptyList(),
    _customArguments: Map<String, @Contextual Any>,
) {
    internal var workflow = Workflow(
        name = name,
        on = on,
        env = env,
        sourceFile = sourceFile,
        targetFileName = targetFileName,
        jobs = jobs,
        concurrency = concurrency,
        yamlConsistencyJobCondition = yamlConsistencyJobCondition,
        _customArguments = _customArguments,
    )

    @Suppress("LongParameterList")
    public fun <OUTPUT : JobOutputs> job(
        id: String,
        name: String? = null,
        runsOn: RunnerType,
        needs: List<Job<*>> = emptyList(),
        condition: String? = null,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        strategyMatrix: Map<String, List<String>>? = null,
        _customArguments: Map<String, @Contextual Any> = mapOf(),
        timeoutMinutes: Int? = null,
        concurrency: Concurrency? = null,
        outputs: OUTPUT,
        block: JobBuilder<OUTPUT>.() -> Unit,
    ): Job<OUTPUT> {
        val jobBuilder = JobBuilder(
            id = id,
            name = name,
            runsOn = runsOn,
            needs = needs,
            condition = condition,
            env = env,
            strategyMatrix = strategyMatrix,
            timeoutMinutes = timeoutMinutes,
            concurrency = concurrency,
            jobOutputs = outputs,
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

    public fun job(
        id: String,
        name: String? = null,
        runsOn: RunnerType,
        needs: List<Job<*>> = emptyList(),
        condition: String? = null,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        strategyMatrix: Map<String, List<String>>? = null,
        _customArguments: Map<String, @Contextual Any> = mapOf(),
        timeoutMinutes: Int? = null,
        concurrency: Concurrency? = null,
        block: JobBuilder<JobOutputs.EMPTY>.() -> Unit,
    ): Job<JobOutputs.EMPTY> = job(
        id = id,
        name = name,
        runsOn = runsOn,
        needs = needs,
        condition = condition,
        env = env,
        strategyMatrix = strategyMatrix,
        _customArguments = _customArguments,
        timeoutMinutes = timeoutMinutes,
        concurrency = concurrency,
        outputs = JobOutputs.EMPTY,
        block = block,
    )

    public fun build(): Workflow = workflow
}

public fun Workflow.toBuilder(): WorkflowBuilder =
    WorkflowBuilder(
        name = name,
        on = on,
        sourceFile = sourceFile,
        targetFileName = targetFileName,
        jobs = jobs,
        _customArguments = _customArguments,
    )

@Suppress("LongParameterList", "FunctionParameterNaming")
public fun workflow(
    name: String,
    on: List<Trigger>,
    env: LinkedHashMap<String, String> = linkedMapOf(),
    sourceFile: Path,
    targetFileName: String = sourceFile.fileName.toString().substringBeforeLast(".main.kts") + ".yaml",
    concurrency: Concurrency? = null,
    yamlConsistencyJobCondition: String? = null,
    _customArguments: Map<String, @Contextual Any> = mapOf(),
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
        targetFileName = targetFileName,
        concurrency = concurrency,
        yamlConsistencyJobCondition = yamlConsistencyJobCondition,
        _customArguments = _customArguments,
    )
    workflowBuilder.block()

    require(workflowBuilder.workflow.jobs.isNotEmpty()) {
        "There are no jobs defined!"
    }
    workflowBuilder.workflow.jobs.requireUniqueJobIds()

    return workflowBuilder.build()
}

private fun List<Job<*>>.requireUniqueJobIds() {
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
