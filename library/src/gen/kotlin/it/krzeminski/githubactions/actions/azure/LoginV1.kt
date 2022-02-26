// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.azure

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress

/**
 * Action: Azure Login
 *
 * Authenticate to Azure and run your Az CLI or Az PowerShell based Actions or scripts. github.com/Azure/Actions
 *
 * [Action on GitHub](https://github.com/Azure/login)
 */
public class LoginV1(
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
     * Set this value to true to enable Azure PowerShell Login in addition to Az CLI login
     */
    public val enableAzPSSession: Boolean? = null,
    /**
     * Name of the environment. Supported values are azurecloud, azurestack, azureusgovernment,
     * azurechinacloud, azuregermancloud. Default being azurecloud
     */
    public val environment: LoginV1.Environment? = null,
    /**
     * Set this value to true to enable support for accessing tenants without subscriptions
     */
    public val allowNoSubscriptions: Boolean? = null,
    /**
     * Provide audience field for access-token. Default value is api://AzureADTokenExchange
     */
    public val audience: String? = null
) : Action("Azure", "login", "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            creds?.let { "creds" to it },
            clientId?.let { "client-id" to it },
            tenantId?.let { "tenant-id" to it },
            subscriptionId?.let { "subscription-id" to it },
            enableAzPSSession?.let { "enable-AzPSSession" to it.toString() },
            environment?.let { "environment" to it.stringValue },
            allowNoSubscriptions?.let { "allow-no-subscriptions" to it.toString() },
            audience?.let { "audience" to it },
        ).toTypedArray()
    )

    public sealed class Environment(
        public val stringValue: String
    ) {
        public object Azurecloud : LoginV1.Environment("azurecloud")

        public object Azurestack : LoginV1.Environment("azurestack")

        public object Azureusgovernment : LoginV1.Environment("azureusgovernment")

        public object Azurechinacloud : LoginV1.Environment("azurechinacloud")

        public object Azuregermancloud : LoginV1.Environment("azuregermancloud")

        public class Custom(
            customStringValue: String
        ) : LoginV1.Environment(customStringValue)
    }
}
