package io.github.typesafegithub.workflows.domain

public sealed interface Shell {
    public data class Custom(val value: String) : Shell

    public object Bash : Shell
    public object Cmd : Shell
    public object Pwsh : Shell
    public object PowerShell : Shell
    public object Python : Shell
    public object Sh : Shell
}
