package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.Container
import io.github.typesafegithub.workflows.domain.Environment
import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.Trigger
import io.github.typesafegithub.workflows.shared.internal.findGitRoot
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig.Disabled
import io.github.typesafegithub.workflows.yaml.DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG
import io.github.typesafegithub.workflows.yaml.Preamble
import io.github.typesafegithub.workflows.yaml.writeToFile
import kotlinx.serialization.Contextual
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolute

@GithubActionsDsl
@Suppress("LongParameterList", "FunctionParameterNaming", "ConstructorParameterNaming")
public class WorkflowBuilder(
    name: String,
    on: List<Trigger>,
    env: Map<String, String> = mapOf(),
    sourceFile: File?,
    targetFileName: String?,
    consistencyCheckJobConfig: ConsistencyCheckJobConfig,
    concurrency: Concurrency? = null,
    public val gitRootDir: Path? = null,
    jobs: List<Job<*>> = emptyList(),
    permissions: Map<Permission, Mode>? = null,
    _customArguments: Map<String, @Contextual Any?>,
) {
    internal var workflow =
        Workflow(
            name = name,
            on = on,
            env = env,
            sourceFile = sourceFile,
            targetFileName = targetFileName,
            jobs = jobs,
            permissions = permissions,
            concurrency = concurrency,
            consistencyCheckJobConfig = consistencyCheckJobConfig,
            _customArguments = _customArguments,
        )

    @Suppress("LongParameterList")
    public fun <OUTPUT : JobOutputs> job(
        @Suppress("UNUSED_PARAMETER")
        vararg pleaseUseNamedArguments: Unit,
        id: String,
        name: String? = null,
        runsOn: RunnerType,
        needs: List<Job<*>> = emptyList(),
        `if`: String? = null,
        condition: String? = null,
        env: Map<String, String> = mapOf(),
        strategyMatrix: Map<String, List<String>>? = null,
        permissions: Map<Permission, Mode>? = null,
        _customArguments: Map<String, @Contextual Any?> = mapOf(),
        timeoutMinutes: Int? = null,
        concurrency: Concurrency? = null,
        container: Container? = null,
        environment: Environment? = null,
        services: Map<String, Container> = emptyMap(),
        outputs: OUTPUT,
        block: JobBuilder<OUTPUT>.() -> Unit,
    ): Job<OUTPUT> {
        require(!(`if` != null && condition != null)) {
            "Either 'if' or 'condition' have to be set, not both!"
        }
        val jobBuilder =
            JobBuilder(
                id = id,
                name = name,
                runsOn = runsOn,
                needs = needs,
                condition = `if` ?: condition,
                env = env,
                strategyMatrix = strategyMatrix,
                permissions = permissions,
                timeoutMinutes = timeoutMinutes,
                concurrency = concurrency,
                container = container,
                environment = environment,
                services = services,
                jobOutputs = outputs,
                _customArguments = _customArguments,
                workflowBuilder = this,
            )
        jobBuilder.block()
        val newJob = jobBuilder.build()

        require(newJob.steps.isNotEmpty()) {
            "There are no steps defined!"
        }
        newJob.requireUniqueStepIds()

        workflow = workflow.copy(jobs = workflow.jobs + newJob)
        return newJob
    }

    public fun job(
        @Suppress("UNUSED_PARAMETER")
        vararg pleaseUseNamedArguments: Unit,
        id: String,
        name: String? = null,
        runsOn: RunnerType,
        needs: List<Job<*>> = emptyList(),
        `if`: String? = null,
        condition: String? = null,
        env: Map<String, String> = mapOf(),
        strategyMatrix: Map<String, List<String>>? = null,
        permissions: Map<Permission, Mode>? = null,
        _customArguments: Map<String, @Contextual Any?> = mapOf(),
        timeoutMinutes: Int? = null,
        concurrency: Concurrency? = null,
        container: Container? = null,
        environment: Environment? = null,
        services: Map<String, Container> = emptyMap(),
        block: JobBuilder<JobOutputs.EMPTY>.() -> Unit,
    ): Job<JobOutputs.EMPTY> {
        require(!(`if` != null && condition != null)) {
            "Either 'if' or 'condition' have to be set, not both!"
        }
        return job(
            id = id,
            name = name,
            runsOn = runsOn,
            needs = needs,
            condition = `if` ?: condition,
            env = env,
            strategyMatrix = strategyMatrix,
            permissions = permissions,
            _customArguments = _customArguments,
            timeoutMinutes = timeoutMinutes,
            concurrency = concurrency,
            outputs = JobOutputs.EMPTY,
            container = container,
            environment = environment,
            services = services,
            block = block,
        )
    }

    public fun build(): Workflow = workflow
}

public fun Workflow.toBuilder(): WorkflowBuilder =
    WorkflowBuilder(
        name = name,
        on = on,
        sourceFile = sourceFile,
        permissions = permissions,
        targetFileName = targetFileName,
        consistencyCheckJobConfig = consistencyCheckJobConfig,
        jobs = jobs,
        _customArguments = _customArguments,
    )

/**
 * A DSL function that models the top-level parts of the GitHub Actions workflow.
 *
 * @param targetFileName Name of the produced YAML file inside the `.github/workflows` directory. If set to `null`,
 * writing to file is disabled.
 * @param useWorkflow A callback that provides a [Workflow] domain object for further analysis, e.g. some custom
 * processing. The result of this processing isn't taken into account during writing the workflow to YAML, the only
 * requirement is that it cannot fail (throw an exception).
 */
@Suppress("LongParameterList", "FunctionParameterNaming")
public fun workflow(
    @Suppress("UNUSED_PARAMETER")
    vararg pleaseUseNamedArguments: Unit,
    name: String,
    on: List<Trigger>,
    env: Map<String, String> = mapOf(),
    sourceFile: File? = null,
    targetFileName: String? =
        sourceFile?.toPath()?.fileName?.let {
            it.toString().substringBeforeLast(".main.kts") + ".yaml"
        },
    concurrency: Concurrency? = null,
    consistencyCheckJobConfig: ConsistencyCheckJobConfig =
        if (sourceFile != null) {
            DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG
        } else {
            Disabled
        },
    permissions: Map<Permission, Mode>? = null,
    gitRootDir: Path? = sourceFile?.toPath()?.absolute()?.findGitRoot(),
    preamble: Preamble? = null,
    useWorkflow: (Workflow) -> Unit = {},
    getenv: (String) -> String? = { System.getenv(it) },
    _customArguments: Map<String, @Contextual Any> = mapOf(),
    block: WorkflowBuilder.() -> Unit,
) {
    require(on.isNotEmpty()) {
        "There are no triggers defined!"
    }

    val workflowBuilder =
        WorkflowBuilder(
            name = name,
            on = on,
            env = env,
            sourceFile = sourceFile,
            targetFileName = targetFileName,
            permissions = permissions,
            concurrency = concurrency,
            gitRootDir = gitRootDir,
            consistencyCheckJobConfig = consistencyCheckJobConfig,
            _customArguments = _customArguments,
        )
    workflowBuilder.block()

    require(workflowBuilder.workflow.jobs.isNotEmpty()) {
        "There are no jobs defined!"
    }
    workflowBuilder.workflow.jobs.requireUniqueJobIds()

    val workflow = workflowBuilder.build()

    useWorkflow(workflow)
    if (targetFileName != null) {
        workflow.writeToFile(
            gitRootDir = gitRootDir,
            preamble = preamble,
            getenv = getenv,
        )
    }
}

private fun List<Job<*>>.requireUniqueJobIds() {
    val duplicatedJobNames =
        this
            .map { it.id }
            .findDuplicates()

    require(duplicatedJobNames.isEmpty()) {
        "Duplicated job names: $duplicatedJobNames"
    }
}

private fun Job<*>.requireUniqueStepIds() {
    val duplicatedStepIds =
        this.steps
            .map { it.id }
            .findDuplicates()

    require(duplicatedStepIds.isEmpty()) {
        "Duplicated step IDs for job '${this.id}': $duplicatedStepIds"
    }
}

private fun <T> Iterable<T>.findDuplicates(): Set<T> =
    this
        .groupingBy { it }
        .eachCount()
        .filter { it.value > 1 }
        .keys
