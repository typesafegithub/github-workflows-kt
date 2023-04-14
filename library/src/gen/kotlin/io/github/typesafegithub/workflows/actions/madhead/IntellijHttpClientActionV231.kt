// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.madhead

import io.github.typesafegithub.workflows.domain.actions.Action
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: intellij-http-client-action
 *
 * IntelliJ HTTP Client CLI GitHub Action – Run Requests and Tests on CI
 *
 * [Action on GitHub](https://github.com/madhead/intellij-http-client-action)
 */
public data class IntellijHttpClientActionV231 private constructor(
    /**
     * HTTP file paths
     */
    public val files: List<String>,
    /**
     * Number of milliseconds for socket read
     */
    public val socketTimeout: Int? = null,
    /**
     * Number of milliseconds for connection
     */
    public val connectTimeout: Int? = null,
    /**
     * Allow insecure SSL connections
     */
    public val insecure: Boolean? = null,
    /**
     * Name of the environment in config file
     */
    public val env: String? = null,
    /**
     * Name of the public environment file
     */
    public val envFile: String? = null,
    /**
     * Public environment variables ('key=value')
     */
    public val envVariables: List<String>? = null,
    /**
     * Name of the private environment file
     */
    public val privateEnvFile: String? = null,
    /**
     * Private environment variables ('key=value')
     */
    public val privateEnvVariables: List<String>? = null,
    /**
     * Enables Docker mode. Treat 'localhost' as 'host.docker.internal'
     */
    public val dockerMode: Boolean? = null,
    /**
     * Logging level. One of 'BASIC' (default), 'HEADERS', or 'VERBOSE'
     */
    public val logLevel: IntellijHttpClientActionV231.LogLevel? = null,
    /**
     * Creates report about execution in JUnit XML Format. Puts it in folder 'reports' in the
     * current directory
     */
    public val report: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<Action.Outputs>("madhead", "intellij-http-client-action", _customVersion ?: "v231") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        files: List<String>,
        socketTimeout: Int? = null,
        connectTimeout: Int? = null,
        insecure: Boolean? = null,
        env: String? = null,
        envFile: String? = null,
        envVariables: List<String>? = null,
        privateEnvFile: String? = null,
        privateEnvVariables: List<String>? = null,
        dockerMode: Boolean? = null,
        logLevel: IntellijHttpClientActionV231.LogLevel? = null,
        report: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(files=files, socketTimeout=socketTimeout, connectTimeout=connectTimeout,
            insecure=insecure, env=env, envFile=envFile, envVariables=envVariables,
            privateEnvFile=privateEnvFile, privateEnvVariables=privateEnvVariables,
            dockerMode=dockerMode, logLevel=logLevel, report=report, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "files" to files.joinToString(" "),
            socketTimeout?.let { "socket_timeout" to it.toString() },
            connectTimeout?.let { "connect_timeout" to it.toString() },
            insecure?.let { "insecure" to it.toString() },
            env?.let { "env" to it },
            envFile?.let { "env_file" to it },
            envVariables?.let { "env_variables" to it.joinToString("\n") },
            privateEnvFile?.let { "private_env_file" to it },
            privateEnvVariables?.let { "private_env_variables" to it.joinToString("\n") },
            dockerMode?.let { "docker_mode" to it.toString() },
            logLevel?.let { "log_level" to it.stringValue },
            report?.let { "report" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class LogLevel(
        public val stringValue: String,
    ) {
        public object Basic : IntellijHttpClientActionV231.LogLevel("BASIC")

        public object Headers : IntellijHttpClientActionV231.LogLevel("HEADERS")

        public object Verbose : IntellijHttpClientActionV231.LogLevel("VERBOSE")

        public class Custom(
            customStringValue: String,
        ) : IntellijHttpClientActionV231.LogLevel(customStringValue)
    }
}
