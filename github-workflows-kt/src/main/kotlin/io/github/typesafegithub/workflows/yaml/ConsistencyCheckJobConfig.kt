package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.dsl.JobBuilder

public val DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG: ConsistencyCheckJobConfig.Configuration =
    ConsistencyCheckJobConfig.Configuration(
        condition = null,
        env = emptyMap(),
        additionalSteps = null,
    )

public sealed interface ConsistencyCheckJobConfig {
    public data object Disabled : ConsistencyCheckJobConfig

    public data class Configuration(
        val condition: String?,
        val env: Map<String, String>,
        val additionalSteps: (JobBuilder<JobOutputs.EMPTY>.() -> Unit)?,
    ) : ConsistencyCheckJobConfig
}
