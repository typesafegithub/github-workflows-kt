package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.dsl.JobBuilder

public val DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG: ConsistencyCheckJobConfig.Configuration =
    ConsistencyCheckJobConfig.Configuration(
        condition = null,
        env = emptyMap(),
        additionalSteps = null,
        useLocalBindingsServerAsFallback = false,
    )

public sealed interface ConsistencyCheckJobConfig {
    public data object Disabled : ConsistencyCheckJobConfig

    public data class Configuration(
        val condition: String?,
        val env: Map<String, String>,
        val additionalSteps: (JobBuilder<JobOutputs.EMPTY>.() -> Unit)?,
        /**
         * If the script execution step in the consistency check job fails, another attempt to execute is made with a
         * local bindings server running. An assumption is made that the bindings server is under
         * `https://bindings.krzeminski.it/`.
         */
        val useLocalBindingsServerAsFallback: Boolean,
    ) : ConsistencyCheckJobConfig
}
