// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.subosito

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
 * Action: Set up Flutter
 *
 * Setup your runner with Flutter environment
 *
 * [Action on GitHub](https://github.com/subosito/flutter-action)
 *
 * @param channel The Flutter build release channel
 * @param flutterVersion The Flutter version to make available on the path
 * @param flutterVersionFile The pubspec.yaml file with exact Flutter version defined
 * @param architecture The architecture of Flutter SDK executable (x64 or arm64)
 * @param cache Cache the Flutter SDK
 * @param cacheKey Identifier for the Flutter SDK cache
 * @param cachePath Flutter SDK cache path
 * @param pubCacheKey Identifier for the Dart .pub-cache cache
 * @param pubCachePath Flutter pub cache path
 * @param dryRun If true, get outputs but do not install Flutter
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class FlutterActionV2 private constructor(
    /**
     * The Flutter build release channel
     */
    public val channel: FlutterActionV2.Channel? = null,
    /**
     * The Flutter version to make available on the path
     */
    public val flutterVersion: String? = null,
    /**
     * The pubspec.yaml file with exact Flutter version defined
     */
    public val flutterVersionFile: String? = null,
    /**
     * The architecture of Flutter SDK executable (x64 or arm64)
     */
    public val architecture: FlutterActionV2.Architecture? = null,
    /**
     * Cache the Flutter SDK
     */
    public val cache: Boolean? = null,
    /**
     * Identifier for the Flutter SDK cache
     */
    public val cacheKey: String? = null,
    /**
     * Flutter SDK cache path
     */
    public val cachePath: String? = null,
    /**
     * Identifier for the Dart .pub-cache cache
     */
    public val pubCacheKey: String? = null,
    /**
     * Flutter pub cache path
     */
    public val pubCachePath: String? = null,
    /**
     * If true, get outputs but do not install Flutter
     */
    public val dryRun: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<FlutterActionV2.Outputs>("subosito", "flutter-action", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        channel: FlutterActionV2.Channel? = null,
        flutterVersion: String? = null,
        flutterVersionFile: String? = null,
        architecture: FlutterActionV2.Architecture? = null,
        cache: Boolean? = null,
        cacheKey: String? = null,
        cachePath: String? = null,
        pubCacheKey: String? = null,
        pubCachePath: String? = null,
        dryRun: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(channel=channel, flutterVersion=flutterVersion, flutterVersionFile=flutterVersionFile,
            architecture=architecture, cache=cache, cacheKey=cacheKey, cachePath=cachePath,
            pubCacheKey=pubCacheKey, pubCachePath=pubCachePath, dryRun=dryRun,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            channel?.let { "channel" to it.stringValue },
            flutterVersion?.let { "flutter-version" to it },
            flutterVersionFile?.let { "flutter-version-file" to it },
            architecture?.let { "architecture" to it.stringValue },
            cache?.let { "cache" to it.toString() },
            cacheKey?.let { "cache-key" to it },
            cachePath?.let { "cache-path" to it },
            pubCacheKey?.let { "pub-cache-key" to it },
            pubCachePath?.let { "pub-cache-path" to it },
            dryRun?.let { "dry-run" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Channel(
        public val stringValue: String,
    ) {
        public object Stable : FlutterActionV2.Channel("stable")

        public object Beta : FlutterActionV2.Channel("beta")

        public object Master : FlutterActionV2.Channel("master")

        public object Dev : FlutterActionV2.Channel("dev")

        public object Any : FlutterActionV2.Channel("any")

        public class Custom(
            customStringValue: String,
        ) : FlutterActionV2.Channel(customStringValue)
    }

    public sealed class Architecture(
        public val stringValue: String,
    ) {
        public object X64 : FlutterActionV2.Architecture("x64")

        public object Arm64 : FlutterActionV2.Architecture("arm64")

        public class Custom(
            customStringValue: String,
        ) : FlutterActionV2.Architecture(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The selected Flutter release channel
         */
        public val channel: String = "steps.$stepId.outputs.CHANNEL"

        /**
         * The selected Flutter version
         */
        public val version: String = "steps.$stepId.outputs.VERSION"

        /**
         * The selected Flutter CPU architecture
         */
        public val architecture: String = "steps.$stepId.outputs.ARCHITECTURE"

        /**
         * Key used to cache the Flutter SDK
         */
        public val cacheKey: String = "steps.$stepId.outputs.CACHE-KEY"

        /**
         * Path to Flutter SDK
         */
        public val cachePath: String = "steps.$stepId.outputs.CACHE-PATH"

        /**
         * Key used to cache the pub dependencies
         */
        public val pubCacheKey: String = "steps.$stepId.outputs.PUB-CACHE-KEY"

        /**
         * Path to pub cache
         */
        public val pubCachePath: String = "steps.$stepId.outputs.PUB-CACHE-PATH"
    }
}
