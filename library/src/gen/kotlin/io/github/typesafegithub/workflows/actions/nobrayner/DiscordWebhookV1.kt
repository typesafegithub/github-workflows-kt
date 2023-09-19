// This file was generated using 'action-binding-generator' module. Don't change it by hand, your changes will
// be overwritten with the next binding code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.nobrayner

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
 * Action: Discord Workflow Status Notifier
 *
 * A GitHub action to report workflow and job status to a Discord Webhook
 *
 * [Action on GitHub](https://github.com/nobrayner/discord-webhook)
 */
public data class DiscordWebhookV1 private constructor(
    /**
     * GitHub Token
     */
    public val githubToken: String,
    /**
     * The WebHook URL to call. Should be stored in Secrets on the Repository
     */
    public val discordWebhook: String,
    /**
     * Overrides the current username of the webhook
     */
    public val username: String? = null,
    /**
     * Overrides the current avatar of the webhook
     */
    public val avatarUrl: String? = null,
    /**
     * Overrides the default title. Include the status in the title by adding the {{STATUS}}
     * placeholder.
     */
    public val title: String? = null,
    /**
     * The message to display. Include the status in the description by adding the {{STATUS}}
     * placeholder
     */
    public val description: String? = null,
    /**
     * Whether or not to include the individual job status breakdown of the Workflow run
     */
    public val includeDetails: Boolean? = null,
    /**
     * Overrides the default success color. Any valid hex-color-code. E.g. #17cf48, 17cf48, 0x17cf48
     */
    public val colorSuccess: String? = null,
    /**
     * Overrides the default failure color. Any valid hex-color-code. E.g. #17cf48, 17cf48, 0x17cf48
     */
    public val colorFailure: String? = null,
    /**
     * Overrides the default cancelled color. Any valid hex-color-code. E.g. #17cf48, 17cf48,
     * 0x17cf48
     */
    public val colorCancelled: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("nobrayner", "discord-webhook", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        githubToken: String,
        discordWebhook: String,
        username: String? = null,
        avatarUrl: String? = null,
        title: String? = null,
        description: String? = null,
        includeDetails: Boolean? = null,
        colorSuccess: String? = null,
        colorFailure: String? = null,
        colorCancelled: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(githubToken=githubToken, discordWebhook=discordWebhook, username=username,
            avatarUrl=avatarUrl, title=title, description=description,
            includeDetails=includeDetails, colorSuccess=colorSuccess, colorFailure=colorFailure,
            colorCancelled=colorCancelled, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "github-token" to githubToken,
            "discord-webhook" to discordWebhook,
            username?.let { "username" to it },
            avatarUrl?.let { "avatar-url" to it },
            title?.let { "title" to it },
            description?.let { "description" to it },
            includeDetails?.let { "include-details" to it.toString() },
            colorSuccess?.let { "color-success" to it },
            colorFailure?.let { "color-failure" to it },
            colorCancelled?.let { "color-cancelled" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
