// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.azure

import it.krzeminski.githubactions.actions.ActionWithOutputs
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Azure WebApp
 *
 * Deploy Web Apps/Containerized Web Apps to Azure. github.com/Azure/Actions
 *
 * [Action on GitHub](https://github.com/Azure/webapps-deploy)
 */
public class WebappsDeployV2(
    /**
     * Name of the Azure Web App
     */
    public val appName: String,
    /**
     * Applies to Web Apps(Windows and Linux) and Web App Containers(linux). Multi container
     * scenario not supported. Publish profile (*.publishsettings) file contents with Web Deploy
     * secrets
     */
    public val publishProfile: String? = null,
    /**
     * Enter an existing Slot other than the Production slot
     */
    public val slotName: String? = null,
    /**
     * Applies to Web App only: Path to package or folder. *.zip, *.war, *.jar or a folder to deploy
     */
    public val `package`: String? = null,
    /**
     * Applies to Web App Containers only: Specify the fully qualified container image(s) name. For
     * example, 'myregistry.azurecr.io/nginx:latest' or 'python:3.7.2-alpine/'. For multi-container
     * scenario multiple container image names can be provided (multi-line separated)
     */
    public val images: List<String>,
    /**
     * Applies to Web App Containers only: Path of the Docker-Compose file. Should be a fully
     * qualified path or relative to the default working directory. Required for multi-container
     * scenario
     */
    public val configurationFile: String? = null,
    /**
     * Enter the start up command. For ex. dotnet run or dotnet run
     */
    public val startupCommand: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customArguments: Map<String, String> = mapOf()
) : ActionWithOutputs<WebappsDeployV2.Outputs>("Azure", "webapps-deploy", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "app-name" to appName,
            publishProfile?.let { "publish-profile" to it },
            slotName?.let { "slot-name" to it },
            `package`?.let { "package" to it },
            "images" to images.joinToString("\n"),
            configurationFile?.let { "configuration-file" to it },
            startupCommand?.let { "startup-command" to it },
            *_customArguments.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String) = Outputs(stepId)

    public class Outputs(
        private val stepId: String
    ) {
        /**
         * URL to work with your webapp
         */
        public val webappUrl: String = "steps.$stepId.outputs.webapp-url"

        public operator fun `get`(outputName: String) = "steps.$stepId.outputs.$outputName"
    }
}
