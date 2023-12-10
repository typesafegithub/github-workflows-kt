// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Deprecated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Upload a Build Artifact
 *
 * Upload a build artifact that can be used by subsequent workflow steps
 *
 * [Action on GitHub](https://github.com/actions/upload-artifact)
 */
@Deprecated(
    message = "This action has a newer major version: UploadArtifactV3",
    replaceWith = ReplaceWith("UploadArtifactV3"),
)
public data class UploadArtifactV2 private constructor(
    /**
     * Artifact name
     */
    public val name: String? = null,
    /**
     * A file, directory or wildcard pattern that describes what to upload
     */
    public val path: List<String>,
    /**
     * The desired behavior if no files are found using the provided path.
     * Available Options:
     *   warn: Output a warning but do not fail the action
     *   error: Fail the action with an error message
     *   ignore: Do not output any warnings or errors, the action does not fail
     */
    public val ifNoFilesFound: UploadArtifactV2.BehaviorIfNoFilesFound? = null,
    /**
     * Duration after which artifact will expire in days. 0 means using default retention.
     * Minimum 1 day. Maximum 90 days unless changed from the repository settings page.
     */
    public val retentionDays: UploadArtifactV2.RetentionPeriod? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("actions", "upload-artifact", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        name: String? = null,
        path: List<String>,
        ifNoFilesFound: UploadArtifactV2.BehaviorIfNoFilesFound? = null,
        retentionDays: UploadArtifactV2.RetentionPeriod? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(name=name, path=path, ifNoFilesFound=ifNoFilesFound, retentionDays=retentionDays,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            name?.let { "name" to it },
            "path" to path.joinToString("\n"),
            ifNoFilesFound?.let { "if-no-files-found" to it.stringValue },
            retentionDays?.let { "retention-days" to it.integerValue.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class BehaviorIfNoFilesFound(
        public val stringValue: String,
    ) {
        public object Warn : UploadArtifactV2.BehaviorIfNoFilesFound("warn")

        public object Error : UploadArtifactV2.BehaviorIfNoFilesFound("error")

        public object Ignore : UploadArtifactV2.BehaviorIfNoFilesFound("ignore")

        public class Custom(
            customStringValue: String,
        ) : UploadArtifactV2.BehaviorIfNoFilesFound(customStringValue)
    }

    public sealed class RetentionPeriod(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : UploadArtifactV2.RetentionPeriod(requestedValue)

        public object Default : UploadArtifactV2.RetentionPeriod(0)
    }
}
