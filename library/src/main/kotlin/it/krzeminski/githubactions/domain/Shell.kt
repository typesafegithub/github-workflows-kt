package it.krzeminski.githubactions.domain

sealed interface Shell {
    data class Custom(val value: String) : Shell

    object Bash : Shell
    object Cmd : Shell
    object Pwsh : Shell
    object PowerShell : Shell
    object Python : Shell
    object Sh : Shell
}
