// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package it.krzeminski.githubactions.actions.azure

import it.krzeminski.githubactions.domain.actions.Action
import it.krzeminski.githubactions.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
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
public data class WebappsDeployV2 private constructor(
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
     * Enter the resource group name of the web app
     */
    public val resourceGroupName: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<WebappsDeployV2.Outputs>("Azure", "webapps-deploy", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        appName: String,
        publishProfile: String? = null,
        slotName: String? = null,
        `package`: String? = null,
        images: List<String>,
        configurationFile: String? = null,
        startupCommand: String? = null,
        resourceGroupName: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(appName=appName, publishProfile=publishProfile, slotName=slotName, `package`=`package`,
            images=images, configurationFile=configurationFile, startupCommand=startupCommand,
            resourceGroupName=resourceGroupName, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "app-name" to appName,
            publishProfile?.let { "publish-profile" to it },
            slotName?.let { "slot-name" to it },
            `package`?.let { "package" to it },
            "images" to images.joinToString("\n"),
            configurationFile?.let { "configuration-file" to it },
            startupCommand?.let { "startup-command" to it },
            resourceGroupName?.let { "resource-group-name" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * URL to work with your webapp
         */
        public val webappUrl: String = "steps.$stepId.outputs.webapp-url"
    }
}
