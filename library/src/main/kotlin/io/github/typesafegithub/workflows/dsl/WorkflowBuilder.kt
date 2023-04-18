package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.Trigger
import io.github.typesafegithub.workflows.internal.either
import kotlinx.serialization.Contextual
import java.nio.file.Path

@GithubActionsDsl
@Suppress("LongParameterList", "FunctionParameterNaming", "ConstructorParameterNaming")
public class WorkflowBuilder(
    name: String,
    on: List<Trigger>,
    env: LinkedHashMap<String, String> = linkedMapOf(),
    sourceFile: Path?,
    targetFileName: String?,
    concurrency: Concurrency? = null,
    yamlConsistencyJobCondition: String? = null,
    jobs: List<Job<*>> = emptyList(),
    permissions: Map<Permission, Mode>? = null,
    _customArguments: Map<String, @Contextual Any?>,
) {
    internal var workflow = Workflow(
        name = name,
        on = on,
        env = env,
        sourceFile = sourceFile,
        targetFileName = targetFileName,
        jobs = jobs,
        permissions = permissions,
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
        `if`: String? = null,
        condition: String? = null,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        strategyMatrix: Map<String, List<String>>? = null,
        permissions: Map<Permission, Mode>? = null,
        _customArguments: Map<String, @Contextual Any?> = mapOf(),
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
            condition = either("if" to `if`, "condition" to condition),
            env = env,
            strategyMatrix = strategyMatrix,
            permissions = permissions,
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
        `if`: String? = null,
        condition: String? = null,
        env: LinkedHashMap<String, String> = linkedMapOf(),
        strategyMatrix: Map<String, List<String>>? = null,
        permissions: Map<Permission, Mode>? = null,
        _customArguments: Map<String, @Contextual Any?> = mapOf(),
        timeoutMinutes: Int? = null,
        concurrency: Concurrency? = null,
        block: JobBuilder<JobOutputs.EMPTY>.() -> Unit,
    ): Job<JobOutputs.EMPTY> = job(
        id = id,
        name = name,
        runsOn = runsOn,
        needs = needs,
        condition = either("if" to `if`, "condition" to condition),
        env = env,
        strategyMatrix = strategyMatrix,
        permissions = permissions,
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
        permissions = permissions,
        targetFileName = targetFileName,
        jobs = jobs,
        _customArguments = _customArguments,
    )

@Suppress("LongParameterList", "FunctionParameterNaming")
public fun workflow(
    name: String,
    on: List<Trigger>,
    env: LinkedHashMap<String, String> = linkedMapOf(),
    sourceFile: Path? = null,
    targetFileName: String? = sourceFile?.fileName?.let { it.toString().substringBeforeLast(".main.kts") + ".yaml" },
    concurrency: Concurrency? = null,
    yamlConsistencyJobCondition: String? = null,
    permissions: Map<Permission, Mode>? = null,
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
        permissions = permissions,
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
