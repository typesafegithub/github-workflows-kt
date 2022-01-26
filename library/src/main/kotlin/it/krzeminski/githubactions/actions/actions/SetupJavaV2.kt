package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action

data class SetupJavaV2(
    val distribution: Distribution,
    val javaVersion: String,
) : Action("actions", "setup-java", "v2") {
    override fun toYamlArguments() = linkedMapOf(
        "distribution" to distribution.toYamlString(),
        "java-version" to javaVersion,
    )

    sealed interface Distribution {
        object Temurin : Distribution
        object Zulu : Distribution
        object Adopt : Distribution
        object AdoptHotspot : Distribution
        object AdoptOpenJ9 : Distribution
        object Liberica : Distribution
        object Microsoft : Distribution
        data class Custom(val name: String) : Distribution
    }

    private fun Distribution.toYamlString(): String =
        when (this) {
            Distribution.Adopt -> "adopt"
            Distribution.AdoptHotspot -> "adopt-hotspot"
            Distribution.AdoptOpenJ9 -> "adopt-openj9"
            Distribution.Liberica -> "liberica"
            Distribution.Microsoft -> "microsoft"
            Distribution.Temurin -> "temurin"
            Distribution.Zulu -> "zulu"
            is Distribution.Custom -> name
        }
}
