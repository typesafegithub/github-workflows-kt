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
public data class UploadArtifactV3 private constructor(
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
    public val ifNoFilesFound: UploadArtifactV3.BehaviorIfNoFilesFound? = null,
    /**
     * Duration after which artifact will expire in days. 0 means using default retention.
     * Minimum 1 day. Maximum 90 days unless changed from the repository settings page.
     */
    public val retentionDays: UploadArtifactV3.RetentionPeriod? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<Action.Outputs>("actions", "upload-artifact", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        name: String? = null,
        path: List<String>,
        ifNoFilesFound: UploadArtifactV3.BehaviorIfNoFilesFound? = null,
        retentionDays: UploadArtifactV3.RetentionPeriod? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(name=name, path=path, ifNoFilesFound=ifNoFilesFound, retentionDays=retentionDays,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            name?.let { "name" to it },
            "path" to path.joinToString("\n"),
            ifNoFilesFound?.let { "if-no-files-found" to it.stringValue },
            retentionDays?.let { "retention-days" to it.integerValue.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class BehaviorIfNoFilesFound(
        public val stringValue: String,
    ) {
        public object Warn : UploadArtifactV3.BehaviorIfNoFilesFound("warn")

        public object Error : UploadArtifactV3.BehaviorIfNoFilesFound("error")

        public object Ignore : UploadArtifactV3.BehaviorIfNoFilesFound("ignore")

        public class Custom(
            customStringValue: String,
        ) : UploadArtifactV3.BehaviorIfNoFilesFound(customStringValue)
    }

    public sealed class RetentionPeriod(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : UploadArtifactV3.RetentionPeriod(requestedValue)

        public object Default : UploadArtifactV3.RetentionPeriod(0)
    }
}
