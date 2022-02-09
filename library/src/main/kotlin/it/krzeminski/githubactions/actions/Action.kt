package it.krzeminski.githubactions.actions

abstract class Action(
    val actionOwner: String,
    val actionName: String,
    val actionVersion: String,
) {
    /**
     * Should probably be implemented with [yamlOf]
     */
    abstract fun toYamlArguments(): LinkedHashMap<String, String>

    protected fun yamlOf(vararg pairs: Pair<String, Any?>): LinkedHashMap<String, String> {
        val errors = mutableMapOf<String, Any>()

        val validPairs = pairs.mapNotNull { (key, value) ->
            val converted = when (value) {
                null -> null
                is String -> value
                is Int -> value.toString()
                is Boolean -> value.toString()
                is HasYaml -> value.yaml
                else -> {
                    errors[key] = value
                    null
                }
            }
            if (converted == null) null else Pair(key, converted)
        }

        require(errors.isEmpty()) { "Invalid YAML properties: $errors" }

        return LinkedHashMap(validPairs.toMap())
    }
}

val Action.fullName: String
    get() = "$actionOwner/$actionName@$actionVersion"

interface HasYaml {
    val yaml: String
}
