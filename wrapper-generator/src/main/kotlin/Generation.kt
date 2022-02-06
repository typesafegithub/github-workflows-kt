data class Wrapper(
    val kotlinCode: String,
    val filePath: String,
)

fun ActionCoords.generateWrapper(
    fetchManifestImpl: ActionCoords.() -> Manifest = { fetchManifest() },
): Wrapper {
    val manifest = fetchManifestImpl()
    val code = buildString {
        appendLine("package it.krzeminski.githubactions.actions.actions")
        appendLine()
        appendLine("import it.krzeminski.githubactions.actions.Action")
        appendLine()
        appendLine("class ${this@generateWrapper.name}(")

        manifest.inputs.forEach { input ->
            appendLine("    val ${input.key}: String,")
        }

        appendLine(") : Action(\"${this@generateWrapper.owner}\", \"${this@generateWrapper.name}\", \"${this@generateWrapper.version}\") {")
        appendLine("    override fun toYamlArguments() = linkedMapOf(")

        manifest.inputs.forEach { input ->
            appendLine("    \"${input.key}\" to ${input.key},")
        }

        appendLine("    )")
        appendLine("}")
    }
    return Wrapper(
        kotlinCode = code,
        filePath = "library/src/main/kotlin/it/krzeminski/githubactions/actions/$owner/$name$version.kt",
    )
}
