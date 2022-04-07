// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.appleboy

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Telegram Message Notify
 *
 * Sending a Telegram message
 *
 * [Action on GitHub](https://github.com/appleboy/telegram-action)
 */
public class TelegramActionV0(
    /**
     * telegram user
     */
    public val to: String? = null,
    /**
     * telegram token
     */
    public val token: String? = null,
    /**
     * telegram message
     */
    public val message: String? = null,
    /**
     * overwrite the default message template with the contents of the specified file
     */
    public val messageFile: String? = null,
    /**
     * support socks5 proxy URL
     */
    public val socks5: String? = null,
    /**
     * send the photo message.
     */
    public val photo: String? = null,
    /**
     * send the document message.
     */
    public val document: String? = null,
    /**
     * send the sticker message.
     */
    public val sticker: String? = null,
    /**
     * send the audio message.
     */
    public val audio: String? = null,
    /**
     * send the voice message.
     */
    public val voice: String? = null,
    /**
     * send the location message.
     */
    public val location: String? = null,
    /**
     * send the venue message.
     */
    public val venue: String? = null,
    /**
     * send the video message.
     */
    public val video: String? = null,
    /**
     * enable debug mode.
     */
    public val debug: Boolean? = null,
    /**
     * message format: markdown or html
     */
    public val format: TelegramActionV0.Format? = null,
    /**
     * disables link previews for links in this message
     */
    public val disableWebPagePreview: Boolean? = null,
    /**
     * disables notifications for this message, supports sending a message without notification,
     */
    public val disableNotification: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("appleboy", "telegram-action", _customVersion ?: "v0.1.1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            to?.let { "to" to it },
            token?.let { "token" to it },
            message?.let { "message" to it },
            messageFile?.let { "message_file" to it },
            socks5?.let { "socks5" to it },
            photo?.let { "photo" to it },
            document?.let { "document" to it },
            sticker?.let { "sticker" to it },
            audio?.let { "audio" to it },
            voice?.let { "voice" to it },
            location?.let { "location" to it },
            venue?.let { "venue" to it },
            video?.let { "video" to it },
            debug?.let { "debug" to it.toString() },
            format?.let { "format" to it.stringValue },
            disableWebPagePreview?.let { "disable_web_page_preview" to it.toString() },
            disableNotification?.let { "disable_notification" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public sealed class Format(
        public val stringValue: String,
    ) {
        public object Markdown : TelegramActionV0.Format("markdown")

        public object Html : TelegramActionV0.Format("html")

        public class Custom(
            customStringValue: String,
        ) : TelegramActionV0.Format(customStringValue)
    }
}
