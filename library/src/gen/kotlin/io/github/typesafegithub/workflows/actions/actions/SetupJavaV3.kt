// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Setup Java JDK
 *
 * Set up a specific version of the Java JDK and add the command-line tools to the PATH
 *
 * [Action on GitHub](https://github.com/actions/setup-java)
 */
public data class SetupJavaV3 private constructor(
    /**
     * The Java version to set up. Takes a whole or semver Java version. See examples of supported
     * syntax in README file
     */
    public val javaVersion: String? = null,
    /**
     * The path to the `.java-version` file. See examples of supported syntax in README file
     */
    public val javaVersionFile: String? = null,
    /**
     * Java distribution. See the list of supported distributions in README file
     */
    public val distribution: SetupJavaV3.Distribution,
    /**
     * The package type (jdk, jre, jdk+fx, jre+fx)
     */
    public val javaPackage: SetupJavaV3.JavaPackage? = null,
    /**
     * The architecture of the package (defaults to the action runner's architecture)
     */
    public val architecture: String? = null,
    /**
     * Path to where the compressed JDK is located
     */
    public val jdkFile: String? = null,
    /**
     * Set this option if you want the action to check for the latest available version that
     * satisfies the version spec
     */
    public val checkLatest: Boolean? = null,
    /**
     * ID of the distributionManagement repository in the pom.xml file. Default is `github`
     */
    public val serverId: String? = null,
    /**
     * Environment variable name for the username for authentication to the Apache Maven repository.
     * Default is $GITHUB_ACTOR
     */
    public val serverUsername: String? = null,
    /**
     * Environment variable name for password or token for authentication to the Apache Maven
     * repository. Default is $GITHUB_TOKEN
     */
    public val serverPassword: String? = null,
    /**
     * Path to where the settings.xml file will be written. Default is ~/.m2.
     */
    public val settingsPath: String? = null,
    /**
     * Overwrite the settings.xml file if it exists. Default is "true".
     */
    public val overwriteSettings: Boolean? = null,
    /**
     * GPG private key to import. Default is empty string.
     */
    public val gpgPrivateKey: String? = null,
    /**
     * Environment variable name for the GPG private key passphrase. Default is $GPG_PASSPHRASE.
     */
    public val gpgPassphrase: String? = null,
    /**
     * Name of the build platform to cache dependencies. It can be "maven", "gradle" or "sbt".
     */
    public val cache: SetupJavaV3.BuildPlatform? = null,
    /**
     * Workaround to pass job status to post job step. This variable is not intended for manual
     * setting
     */
    public val jobStatus: String? = null,
    /**
     * The token used to authenticate when fetching version manifests hosted on github.com, such as
     * for the Microsoft Build of OpenJDK. When running this action on github.com, the default value is
     * sufficient. When running on GHES, you can pass a personal access token for github.com if you are
     * experiencing rate limiting.
     */
    public val token: String? = null,
    /**
     * Name of Maven Toolchain ID if the default name of "${distribution}_${java-version}" is not
     * wanted. See examples of supported syntax in Advanced Usage file
     */
    public val mvnToolchainId: String? = null,
    /**
     * Name of Maven Toolchain Vendor if the default name of "${distribution}" is not wanted. See
     * examples of supported syntax in Advanced Usage file
     */
    public val mvnToolchainVendor: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<SetupJavaV3.Outputs>("actions", "setup-java", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        javaVersion: String? = null,
        javaVersionFile: String? = null,
        distribution: SetupJavaV3.Distribution,
        javaPackage: SetupJavaV3.JavaPackage? = null,
        architecture: String? = null,
        jdkFile: String? = null,
        checkLatest: Boolean? = null,
        serverId: String? = null,
        serverUsername: String? = null,
        serverPassword: String? = null,
        settingsPath: String? = null,
        overwriteSettings: Boolean? = null,
        gpgPrivateKey: String? = null,
        gpgPassphrase: String? = null,
        cache: SetupJavaV3.BuildPlatform? = null,
        jobStatus: String? = null,
        token: String? = null,
        mvnToolchainId: String? = null,
        mvnToolchainVendor: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(javaVersion=javaVersion, javaVersionFile=javaVersionFile, distribution=distribution,
            javaPackage=javaPackage, architecture=architecture, jdkFile=jdkFile,
            checkLatest=checkLatest, serverId=serverId, serverUsername=serverUsername,
            serverPassword=serverPassword, settingsPath=settingsPath,
            overwriteSettings=overwriteSettings, gpgPrivateKey=gpgPrivateKey,
            gpgPassphrase=gpgPassphrase, cache=cache, jobStatus=jobStatus, token=token,
            mvnToolchainId=mvnToolchainId, mvnToolchainVendor=mvnToolchainVendor,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            javaVersion?.let { "java-version" to it },
            javaVersionFile?.let { "java-version-file" to it },
            "distribution" to distribution.stringValue,
            javaPackage?.let { "java-package" to it.stringValue },
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
            cache?.let { "cache" to it.stringValue },
            jobStatus?.let { "job-status" to it },
            token?.let { "token" to it },
            mvnToolchainId?.let { "mvn-toolchain-id" to it },
            mvnToolchainVendor?.let { "mvn-toolchain-vendor" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Distribution(
        public val stringValue: String,
    ) {
        public object Adopt : SetupJavaV3.Distribution("adopt")

        public object AdoptHotspot : SetupJavaV3.Distribution("adopt-hotspot")

        public object AdoptOpenj9 : SetupJavaV3.Distribution("adopt-openj9")

        public object Corretto : SetupJavaV3.Distribution("corretto")

        public object Liberica : SetupJavaV3.Distribution("liberica")

        public object Microsoft : SetupJavaV3.Distribution("microsoft")

        public object Temurin : SetupJavaV3.Distribution("temurin")

        public object Zulu : SetupJavaV3.Distribution("zulu")

        public class Custom(
            customStringValue: String,
        ) : SetupJavaV3.Distribution(customStringValue)
    }

    public sealed class JavaPackage(
        public val stringValue: String,
    ) {
        public object Jdk : SetupJavaV3.JavaPackage("jdk")

        public object Jre : SetupJavaV3.JavaPackage("jre")

        public object JdkPlusFx : SetupJavaV3.JavaPackage("jdk+fx")

        public object JrePlusFx : SetupJavaV3.JavaPackage("jre+fx")

        public class Custom(
            customStringValue: String,
        ) : SetupJavaV3.JavaPackage(customStringValue)
    }

    public sealed class BuildPlatform(
        public val stringValue: String,
    ) {
        public object Maven : SetupJavaV3.BuildPlatform("maven")

        public object Gradle : SetupJavaV3.BuildPlatform("gradle")

        public object Sbt : SetupJavaV3.BuildPlatform("sbt")

        public class Custom(
            customStringValue: String,
        ) : SetupJavaV3.BuildPlatform(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * Distribution of Java that has been installed
         */
        public val distribution: String = "steps.$stepId.outputs.distribution"

        /**
         * Actual version of the java environment that has been installed
         */
        public val version: String = "steps.$stepId.outputs.version"

        /**
         * Path to where the java environment has been installed (same as $JAVA_HOME)
         */
        public val path: String = "steps.$stepId.outputs.path"

        /**
         * A boolean value to indicate an exact match was found for the primary key
         */
        public val cacheHit: String = "steps.$stepId.outputs.cache-hit"
    }
}
