package it.krzeminski.githubactions.actions.madhead

import it.krzeminski.githubactions.actions.Action

data class SemverUtils(
    val version: String,
    val compareTo: String? = null,
    val satisfies: String? = null,
    val identifier: String? = null,
) : Action(name = "madhead/semver-utils@latest") {
    override fun toYamlArguments(): LinkedHashMap<String, String> {
        val result = LinkedHashMap<String, String>()

        result["version"] = version
        compareTo?.let { result["compareTo"] = it }
        satisfies?.let { result["satisfies"] = it }
        identifier?.let { result["identifier"] = it }

        return result
    }
}
