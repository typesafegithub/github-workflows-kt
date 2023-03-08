// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.domain.actions.Action
import it.krzeminski.githubactions.domain.actions.Action.Outputs
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
 * Action: Save a cache
 *
 * Save Cache artifacts like dependencies and build outputs to improve workflow execution time
 *
 * [Action on GitHub](https://github.com/actions/cache/tree/v3/save)
 */
public data class CacheSaveV3 private constructor(
    /**
     * A list of files, directories, and wildcard patterns to cache
     */
    public val path: List<String>,
    /**
     * An explicit key for saving the cache
     */
    public val key: String,
    /**
     * The chunk size used to split up large files during upload, in bytes
     */
    public val uploadChunkSize: Int? = null,
    /**
     * An optional boolean when enabled, allows windows runners to save caches that can be restored
     * on other platforms
     */
    public val enableCrossOsArchive: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<Action.Outputs>("actions", "cache/save", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        path: List<String>,
        key: String,
        uploadChunkSize: Int? = null,
        enableCrossOsArchive: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(path=path, key=key, uploadChunkSize=uploadChunkSize,
            enableCrossOsArchive=enableCrossOsArchive, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "path" to path.joinToString("\n"),
            "key" to key,
            uploadChunkSize?.let { "upload-chunk-size" to it.toString() },
            enableCrossOsArchive?.let { "enableCrossOsArchive" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
