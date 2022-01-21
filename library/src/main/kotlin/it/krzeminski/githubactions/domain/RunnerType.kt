package it.krzeminski.githubactions.domain

/**
 * Types of GitHub-hosted runners available. Should be kept in sync with the official list at
 * https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#choosing-github-hosted-runners
 */
enum class RunnerType {
    // "Latest" labels
    UbuntuLatest,
    WindowsLatest,
    MacOSLatest,

    // Windows runners
    Windows2022,
    Windows2019,
    Windows2016,

    // Ubuntu runners
    Ubuntu2004,
    Ubuntu1804,

    // macOS runners
    MacOS11,
    MacOS1015,
}
