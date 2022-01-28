package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action

data class SetupJavaV2(
    val distribution: Distribution,
    val javaVersion: String,
    val javaPackage: JavaPackage? = null,
    val architecture: String? = null,
    val jdkFile: String? = null,
    val checkLatest: Boolean? = null,
    val serverId: String? = null,
    val serverUsername: String? = null,
    val serverPassword: String? = null,
    val settingsPath: String? = null,
    val overwriteSettings: Boolean? = null,
    val gpgPrivateKey: String? = null,
    val gpgPassphrase: String? = null,
    val cache: BuildPlatform? = null,
) : Action("actions", "setup-java", "v2") {
    @Suppress("SpreadOperator")
    override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "distribution" to distribution.toYamlString(),
            "java-version" to javaVersion,
            javaPackage?.let { "java-package" to it.toYamlString() },
            architecture?.let { "architecture" to it },
            jdkFile?.let { "jdkFile" to it },
            checkLatest?.let { "check-latest" to it.toString() },
            serverId?.let { "server-id" to it },
            serverUsername?.let { "server-username" to it },
            serverPassword?.let { "server-password" to it },
            settingsPath?.let { "settings-path" to it },
            overwriteSettings?.let { "overwrite-settings" to it.toString() },
            gpgPrivateKey?.let { "gpg-private-key" to it },
            gpgPassphrase?.let { "gpg-passphrase" to it },
            cache?.let { "cache" to it.toYamlString() },
        ).toTypedArray()
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

    sealed interface JavaPackage {
        object Jdk : JavaPackage
        object Jre : JavaPackage
        object JdkPlusFx : JavaPackage
        object JrePlusFx : JavaPackage
        data class Custom(val name: String) : JavaPackage
    }

    private fun JavaPackage.toYamlString(): String =
        when (this) {
            JavaPackage.Jdk -> "jdk"
            JavaPackage.Jre -> "jre"
            JavaPackage.JdkPlusFx -> "jdk+fx"
            JavaPackage.JrePlusFx -> "jre+fx"
            is JavaPackage.Custom -> name
        }

    sealed interface BuildPlatform {
        object Maven : BuildPlatform
        object Gradle : BuildPlatform
        data class Custom(val name: String) : BuildPlatform
    }

    private fun BuildPlatform.toYamlString(): String =
        when (this) {
            BuildPlatform.Maven -> "maven"
            BuildPlatform.Gradle -> "gradle"
            is BuildPlatform.Custom -> name
        }
}
