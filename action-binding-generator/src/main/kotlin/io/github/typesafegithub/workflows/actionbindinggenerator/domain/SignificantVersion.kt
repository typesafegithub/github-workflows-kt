package io.github.typesafegithub.workflows.actionbindinggenerator.domain

public enum class SignificantVersion {
    MAJOR,
    MINOR,
    FULL,
    ;

    override fun toString(): String = super.toString().lowercase()
}
