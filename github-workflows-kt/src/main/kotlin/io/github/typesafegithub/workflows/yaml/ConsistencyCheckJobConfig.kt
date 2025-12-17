package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.dsl.JobBuilder

public val DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG: ConsistencyCheckJobConfig.Configuration =
    ConsistencyCheckJobConfig.Configuration(
        condition = null,
        env = emptyMap(),
        checkoutActionVersion = CheckoutActionVersionSource.BundledWithLibrary,
        checkoutActionClassFQN = "io.github.typesafegithub.workflows.actions.actions.Checkout",
        additionalSteps = null,
        useLocalBindingsServerAsFallback = false,
    )

public sealed interface ConsistencyCheckJobConfig {
    public data object Disabled : ConsistencyCheckJobConfig

    public data class Configuration(
        val condition: String?,
        val env: Map<String, String>,
        /**
         * Configures what version of https://github.com/actions/checkout is used in the consistency check job.
         * Lets the user choose between convenience of automatic updates and more determinism and control if required.
         */
        val checkoutActionVersion: CheckoutActionVersionSource,
        /**
         * Specifies the fully qualified name of a binding class for actions/checkout. Can be overridden if a custom
         * binding class is provided for this action, under a different FQN.
         */
        val checkoutActionClassFQN: String,
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
    /**
     * Every version of github-workflows-kt library comes with a hardcoded version of this action.
     * Since bumping this version is possible only upon major releases (major version bumps), this
     * version often lags behind the newest one.
     *
     * This is the current default option.
     */
    public object BundledWithLibrary : CheckoutActionVersionSource

    /**
     * In the great majority of workflows, the checkout action is used in a preferred version.
     * We can utilize this fact and use the same version in the consistency check job.
     * Warning: this option won't work if a dependency on "action:checkout:vN" is not present.
     *
     * This is the most convenient option, and a candidate to become the default one.
     */
    public object InferFromClasspath : CheckoutActionVersionSource

    /**
     * Useful if it's desired to specify a concrete version by hand, right in your workflow.
     */
    public class Given(
        public val version: String,
    ) : CheckoutActionVersionSource
}
