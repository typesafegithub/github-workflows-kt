package io.github.typesafegithub.workflows.domain

/**
 * Types of GitHub-hosted runners available. Should be kept in sync with the official list at
 * https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#choosing-github-hosted-runners
 */
public sealed interface RunnerType {
    // Custom runner. Could be an expression `runsOn = expr("github.event.inputs.run-on")`
    public data class Custom(val runsOn: String) : RunnerType

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
}
