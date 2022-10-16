// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.calibreapp

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Image Actions
 *
 * Compresses Images for the Web
 *
 * [Action on GitHub](https://github.com/calibreapp/image-actions)
 */
public class ImageActionsV1(
    /**
     * GitHub Token
     */
    public val githubToken: String,
    /**
     * JPEG quality level
     */
    public val jpegQuality: Int? = null,
    /**
     * PNG quality level
     */
    public val pngQuality: Int? = null,
    /**
     * WEBP quality level
     */
    public val webpQuality: Int? = null,
    /**
     * Paths to ignore during search
     */
    public val ignorePaths: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    _customVersion: String? = null,
) : Action("calibreapp", "image-actions", _customVersion ?: "v1.1.0") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "githubToken" to githubToken,
            jpegQuality?.let { "jpegQuality" to it.toString() },
            pngQuality?.let { "pngQuality" to it.toString() },
            webpQuality?.let { "webpQuality" to it.toString() },
            ignorePaths?.let { "ignorePaths" to it.joinToString("\n") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )
}
