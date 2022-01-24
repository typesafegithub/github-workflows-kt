package it.krzeminski.githubactions.actions.madhead

import it.krzeminski.githubactions.actions.Action

data class ReadJavaProperties(
    val file: String,
    val property: String? = null,
    val all: Boolean? = null,
    val default: String? = null,
) : Action(name = "madhead/read-java-properties@latest") {
    override fun toYamlArguments(): LinkedHashMap<String, String> {
        val result = LinkedHashMap<String, String>()

        result["file"] = file
        property?.let { result["property"] = it }
        all?.let { result["all"] = it.toString() }
        default?.let { result["default"] = it }

        return result
    }
}
