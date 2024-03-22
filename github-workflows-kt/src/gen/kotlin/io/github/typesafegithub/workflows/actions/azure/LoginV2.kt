// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.azure

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Azure Login
 *
 * Authenticate to Azure and run your Azure CLI or Azure PowerShell based actions or scripts.
 *
 * [Action on GitHub](https://github.com/Azure/login)
 *
 * @param creds Paste output of `az ad sp create-for-rbac` as value of secret variable:
 * AZURE_CREDENTIALS
 * @param clientId ClientId of the Azure Service principal created.
 * @param tenantId TenantId of the Azure Service principal created.
 * @param subscriptionId Azure subscriptionId
 * @param enableAzPSSession Set this value to true to enable Azure PowerShell Login in addition to
 * Azure CLI login
 * @param environment Name of the environment. Supported values are azurecloud, azurestack,
 * azureusgovernment, azurechinacloud, azuregermancloud. Default being azurecloud
 * @param allowNoSubscriptions Set this value to true to enable support for accessing tenants
 * without subscriptions
 * @param audience Provide audience field for access-token. Default value is
 * api://AzureADTokenExchange
 * @param authType The type of authentication. Supported values are SERVICE_PRINCIPAL, IDENTITY.
 * Default value is SERVICE_PRINCIPAL
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class LoginV2 private constructor(
    /**
     * Paste output of `az ad sp create-for-rbac` as value of secret variable: AZURE_CREDENTIALS
     */
    public val creds: String? = null,
    /**
     * ClientId of the Azure Service principal created.
     */
    public val clientId: String? = null,
    /**
     * TenantId of the Azure Service principal created.
     */
    public val tenantId: String? = null,
    /**
     * Azure subscriptionId
     */
    public val subscriptionId: String? = null,
    /**
     * Set this value to true to enable Azure PowerShell Login in addition to Azure CLI login
     */
    public val enableAzPSSession: Boolean? = null,
    /**
     * Name of the environment. Supported values are azurecloud, azurestack, azureusgovernment,
     * azurechinacloud, azuregermancloud. Default being azurecloud
     */
    public val environment: LoginV2.Environment? = null,
    /**
     * Set this value to true to enable support for accessing tenants without subscriptions
     */
    public val allowNoSubscriptions: Boolean? = null,
    /**
     * Provide audience field for access-token. Default value is api://AzureADTokenExchange
     */
    public val audience: String? = null,
    /**
     * The type of authentication. Supported values are SERVICE_PRINCIPAL, IDENTITY. Default value
     * is SERVICE_PRINCIPAL
     */
    public val authType: LoginV2.AuthType? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("Azure", "login", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        creds: String? = null,
        clientId: String? = null,
        tenantId: String? = null,
        subscriptionId: String? = null,
        enableAzPSSession: Boolean? = null,
        environment: LoginV2.Environment? = null,
        allowNoSubscriptions: Boolean? = null,
        audience: String? = null,
        authType: LoginV2.AuthType? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(creds=creds, clientId=clientId, tenantId=tenantId, subscriptionId=subscriptionId,
            enableAzPSSession=enableAzPSSession, environment=environment,
            allowNoSubscriptions=allowNoSubscriptions, audience=audience, authType=authType,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            creds?.let { "creds" to it },
            clientId?.let { "client-id" to it },
            tenantId?.let { "tenant-id" to it },
            subscriptionId?.let { "subscription-id" to it },
            enableAzPSSession?.let { "enable-AzPSSession" to it.toString() },
            environment?.let { "environment" to it.stringValue },
            allowNoSubscriptions?.let { "allow-no-subscriptions" to it.toString() },
            audience?.let { "audience" to it },
            authType?.let { "auth-type" to it.stringValue },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Environment(
        public val stringValue: String,
    ) {
        public object Azurecloud : LoginV2.Environment("azurecloud")

        public object Azurestack : LoginV2.Environment("azurestack")

        public object Azureusgovernment : LoginV2.Environment("azureusgovernment")

        public object Azurechinacloud : LoginV2.Environment("azurechinacloud")

        public object Azuregermancloud : LoginV2.Environment("azuregermancloud")

        public class Custom(
            customStringValue: String,
        ) : LoginV2.Environment(customStringValue)
    }

    public sealed class AuthType(
        public val stringValue: String,
    ) {
        public object ServicePrincipal : LoginV2.AuthType("SERVICE_PRINCIPAL")

        public object Identity : LoginV2.AuthType("IDENTITY")

        public class Custom(
            customStringValue: String,
        ) : LoginV2.AuthType(customStringValue)
    }
}
