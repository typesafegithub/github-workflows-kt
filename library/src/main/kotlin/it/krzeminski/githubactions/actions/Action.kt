package it.krzeminski.githubactions.actions

abstract class Action(
    val name: String,
) {
    abstract fun toYamlArguments(): LinkedHashMap<String, String>
}
