package io.github.typesafegithub.workflows.domain

/**
 * Types of GitHub-hosted runners available. Should be kept in sync with the official list at
 * https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#choosing-github-hosted-runners
 */
public sealed interface RunnerType {
    // Custom runner. Could be an expression `runsOn = expr("github.event.inputs.run-on")`
    public data class Custom(
        val runsOn: String,
    ) : RunnerType

    public data class Labelled(
        val labels: Set<String>,
    ) : RunnerType {
        public constructor(vararg labels: String) : this(LinkedHashSet(labels.toList()))

        init {
            require(labels.isNotEmpty()) { "At least one label must be provided" }
        }
    }

    /**
     * @see <a href="https://docs.github.com/en/actions/using-jobs/choosing-the-runner-for-a-job#choosing-runners-in-a-group">Choosing runners in a group</a>
     */
    @Suppress("MaxLineLength")
    public data class Group(
        val name: String,
        val labels: Set<String>? = null,
    ) : RunnerType {
        public constructor(name: String, vararg labels: String) : this(name, LinkedHashSet(labels.toList()))
    }

    // "Latest" labels
    public object UbuntuLatest : RunnerType

    public object WindowsLatest : RunnerType

    public object MacOSLatest : RunnerType

    // Windows runners
    public object Windows2022 : RunnerType

    public object Windows2019 : RunnerType

    public object Windows2016 : RunnerType

    // Ubuntu runners
    public object Ubuntu2004 : RunnerType

    public object Ubuntu1804 : RunnerType

    // macOS runners
    public object MacOS11 : RunnerType

    public object MacOS1015 : RunnerType

    public companion object {
        /**
         * @see <a href="https://docs.github.com/en/actions/using-jobs/choosing-the-runner-for-a-job#choosing-self-hosted-runners">Choosing self-hosted runners</a>
         */
        @Suppress("MaxLineLength")
        public fun selfHosted(vararg labels: String): Labelled = Labelled("self-hosted", *labels)
    }
}
