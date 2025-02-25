package io.github.typesafegithub.workflows.actionbindinggenerator.domain

/**
 * The version part that is significant when generating the YAML output.
 * This is used to enable usage of Maven ranges without needing to specify a custom version
 * each time instantiating an action.
 */
public enum class SignificantVersion {
    /**
     * Only write the major version to the generated YAML.
     */
    MAJOR,

    /**
     * Only write the major and minor version to the generated YAML.
     */
    MINOR,

    /**
     * Write the full version to the generated YAML.
     */
    FULL,
    ;

    override fun toString(): String = super.toString().lowercase()
}
