// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
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
 * Action: Cache
 *
 * Cache artifacts like dependencies and build outputs to improve workflow execution time
 *
 * [Action on GitHub](https://github.com/actions/cache)
 */
public data class CacheV3 private constructor(
    /**
     * A list of files, directories, and wildcard patterns to cache and restore
     */
    public val path: List<String>,
    /**
     * An explicit key for restoring and saving the cache
     */
    public val key: String,
    /**
     * An ordered list of keys to use for restoring stale cache if no cache hit occurred for key.
     * Note `cache-hit` returns false in this case.
     */
    public val restoreKeys: List<String>? = null,
    /**
     * The chunk size used to split up large files during upload, in bytes
     */
    public val uploadChunkSize: Int? = null,
    /**
     * An optional boolean when enabled, allows windows runners to save or restore caches that can
     * be restored or saved respectively on other platforms
     */
    public val enableCrossOsArchive: Boolean? = null,
    /**
     * Fail the workflow if cache entry is not found
     */
    public val failOnCacheMiss: Boolean? = null,
    /**
     * Check if a cache entry exists for the given input(s) (key, restore-keys) without downloading
     * the cache
     */
    public val lookupOnly: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<CacheV3.Outputs>("actions", "cache", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        path: List<String>,
        key: String,
        restoreKeys: List<String>? = null,
        uploadChunkSize: Int? = null,
        enableCrossOsArchive: Boolean? = null,
        failOnCacheMiss: Boolean? = null,
        lookupOnly: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(path=path, key=key, restoreKeys=restoreKeys, uploadChunkSize=uploadChunkSize,
            enableCrossOsArchive=enableCrossOsArchive, failOnCacheMiss=failOnCacheMiss,
            lookupOnly=lookupOnly, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "path" to path.joinToString("\n"),
            "key" to key,
            restoreKeys?.let { "restore-keys" to it.joinToString("\n") },
            uploadChunkSize?.let { "upload-chunk-size" to it.toString() },
            enableCrossOsArchive?.let { "enableCrossOsArchive" to it.toString() },
            failOnCacheMiss?.let { "fail-on-cache-miss" to it.toString() },
            lookupOnly?.let { "lookup-only" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * A boolean value to indicate an exact match was found for the primary key
         */
        public val cacheHit: String = "steps.$stepId.outputs.cache-hit"
    }
}
