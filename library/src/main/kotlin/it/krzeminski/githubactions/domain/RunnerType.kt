package it.krzeminski.githubactions.domain

/**
 * Types of GitHub-hosted runners available. Should be kept in sync with the official list at
 * https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#choosing-github-hosted-runners
 */
sealed interface RunnerType {
    // Custom runner. Could be an expression `runsOn = expr("github.event.inputs.run-on")`
    data class Custom(val runsOn: String) : RunnerType

    // "Latest" labels
    object UbuntuLatest : RunnerType
    object WindowsLatest : RunnerType
    object MacOSLatest : RunnerType

    // Windows runners
    object Windows2022 : RunnerType
    object Windows2019 : RunnerType
    object Windows2016 : RunnerType

    // Ubuntu runners
    object Ubuntu2004 : RunnerType
    object Ubuntu1804 : RunnerType

    // macOS runners
    object MacOS11 : RunnerType
    object MacOS1015 : RunnerType
}
