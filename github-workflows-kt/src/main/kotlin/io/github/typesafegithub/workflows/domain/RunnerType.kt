package io.github.typesafegithub.workflows.domain

/**
 * Types of GitHub-hosted runners available. Should be kept in sync with the official list at
 * https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#choosing-github-hosted-runners
 */
public sealed interface RunnerType {
    public sealed class GitHubHosted(
        public val value: String,
    ) : RunnerType

    // "Latest" labels
    public object UbuntuLatest : GitHubHosted("ubuntu-latest")

    public object WindowsLatest : GitHubHosted("windows-latest")

    public object MacosLatest : GitHubHosted("macos-latest")

    // Windows runners
    public object Windows2025 : GitHubHosted("windows-2025")

    public object Windows2025Vs2026 : GitHubHosted("windows-2025-vs2026")

    public object Windows2022 : GitHubHosted("windows-2022")

    public object Windows11Arm : GitHubHosted("windows-11-arm")

    // Ubuntu runners
    public object UbuntuSlim : GitHubHosted("ubuntu-slim")

    public object Ubuntu2404 : GitHubHosted("ubuntu-24.04")

    public object Ubuntu2404Arm : GitHubHosted("ubuntu-24.04-arm")

    public object Ubuntu2204 : GitHubHosted("ubuntu-22.04")

    public object Ubuntu2204Arm : GitHubHosted("ubuntu-22.04-arm")

    // macOS runners
    public object Macos26 : GitHubHosted("macos-26")

    public object Macos26Intel : GitHubHosted("macos-26-intel")

    public object Macos15 : GitHubHosted("macos-15")

    public object Macos15Intel : GitHubHosted("macos-15-intel")

    public object Macos14 : GitHubHosted("macos-14")

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

    public companion object {
        /**
         * @see <a href="https://docs.github.com/en/actions/using-jobs/choosing-the-runner-for-a-job#choosing-self-hosted-runners">Choosing self-hosted runners</a>
         */
        @Suppress("MaxLineLength")
        public fun selfHosted(vararg labels: String): Labelled = Labelled("self-hosted", *labels)
    }
}
