package it.krzeminski.githubactions.actions.madhead

import it.krzeminski.githubactions.actions.Action

class SemverUtils(
    val version: String,
    val compareTo: String? = null,
    val satisfies: String? = null,
    val identifier: String? = null,
) : Action("madhead", "semver-utils", "latest") {
    override fun toYamlArguments(): LinkedHashMap<String, String> {
        val result = LinkedHashMap<String, String>()

        result["version"] = version
        compareTo?.let { result["compare-to"] = it }
        satisfies?.let { result["satisfies"] = it }
        identifier?.let { result["identifier"] = it }

        return result
    }
}
