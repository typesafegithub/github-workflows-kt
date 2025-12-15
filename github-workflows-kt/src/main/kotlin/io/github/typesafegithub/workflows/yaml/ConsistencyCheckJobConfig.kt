package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.dsl.JobBuilder

public val DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG: ConsistencyCheckJobConfig.Configuration =
    ConsistencyCheckJobConfig.Configuration(
        condition = null,
        env = emptyMap(),
        checkoutActionVersion = CheckoutActionVersionSource.BundledWithLibrary,
        additionalSteps = null,
        useLocalBindingsServerAsFallback = false,
    )

public sealed interface ConsistencyCheckJobConfig {
    public data object Disabled : ConsistencyCheckJobConfig

    public data class Configuration(
        val condition: String?,
        val env: Map<String, String>,
        /**
         * Configures what version of https://github.com/actions/checkout should be used in the consistency check job.
         * Lets the user choose between convenience of automatic updates and more determinism and control if required.
         */
        val checkoutActionVersion: CheckoutActionVersionSource,
        val additionalSteps: (JobBuilder<JobOutputs.EMPTY>.() -> Unit)?,
        /**
         * If the script execution step in the consistency check job fails, another attempt to execute is made with a
         * local bindings server running.
         * An assumption is made that the bindings server is under `https://bindings.krzeminski.it`. It's currently not
         * configurable.
         */
        val useLocalBindingsServerAsFallback: Boolean,
    ) : ConsistencyCheckJobConfig
}

public sealed interface CheckoutActionVersionSource {
    public object BundledWithLibrary : CheckoutActionVersionSource
    public object InferredFromClasspath : CheckoutActionVersionSource
    public class Given(public val version: String) : CheckoutActionVersionSource
}
