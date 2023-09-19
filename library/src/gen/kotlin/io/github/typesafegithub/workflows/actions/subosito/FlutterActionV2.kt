// This file was generated using 'action-binding-generator' module. Don't change it by hand, your changes will
// be overwritten with the next binding code regeneration. Instead, consider introducing changes to the
// generator itself.
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
 * Action: Flutter action
 *
 * Setup your runner with Flutter environment
 *
 * [Action on GitHub](https://github.com/subosito/flutter-action)
 */
public data class FlutterActionV2 private constructor(
    /**
     * The Flutter version to make available on the path
     */
    public val flutterVersion: String? = null,
    /**
     * The Flutter build release channel
     */
    public val channel: FlutterActionV2.Channel? = null,
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
     * The architecture of Flutter SDK executable (x64 or arm64)
     */
    public val architecture: FlutterActionV2.Architecture? = null,
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
        flutterVersion: String? = null,
        channel: FlutterActionV2.Channel? = null,
        cache: Boolean? = null,
        cacheKey: String? = null,
        cachePath: String? = null,
        architecture: FlutterActionV2.Architecture? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(flutterVersion=flutterVersion, channel=channel, cache=cache, cacheKey=cacheKey,
            cachePath=cachePath, architecture=architecture, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            flutterVersion?.let { "flutter-version" to it },
            channel?.let { "channel" to it.stringValue },
            cache?.let { "cache" to it.toString() },
            cacheKey?.let { "cache-key" to it },
            cachePath?.let { "cache-path" to it },
            architecture?.let { "architecture" to it.stringValue },
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
        public val cachePath: String = "steps.$stepId.outputs.CACHE-PATH"

        public val cacheKey: String = "steps.$stepId.outputs.CACHE-KEY"

        public val channel: String = "steps.$stepId.outputs.CHANNEL"

        public val version: String = "steps.$stepId.outputs.VERSION"

        public val architecture: String = "steps.$stepId.outputs.ARCHITECTURE"
    }
}
