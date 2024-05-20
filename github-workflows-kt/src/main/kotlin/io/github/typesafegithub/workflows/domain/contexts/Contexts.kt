package io.github.typesafegithub.workflows.domain.contexts

public data class Contexts(
    val github: GithubContext,
) {
    val outputs: OutputsContext = OutputsContext
}
