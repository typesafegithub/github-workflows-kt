package io.github.typesafegithub.workflows.actionbindinggenerator.versioning

public enum class BindingVersion(
    public val isDeprecated: Boolean = false,
    public val isExperimental: Boolean = true,
    public val libraryVersion: String,
) {
    V1(isExperimental = false, libraryVersion = "3.7.0"),
    ;

    override fun toString(): String = super.toString().lowercase()
}
