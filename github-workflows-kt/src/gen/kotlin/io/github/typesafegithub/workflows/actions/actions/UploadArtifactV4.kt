// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
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
public data class UploadArtifactV4 private constructor(
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
    public val ifNoFilesFound: UploadArtifactV4.BehaviorIfNoFilesFound? = null,
    /**
     * Duration after which artifact will expire in days. 0 means using default retention.
     * Minimum 1 day. Maximum 90 days unless changed from the repository settings page.
     */
    public val retentionDays: UploadArtifactV4.RetentionPeriod? = null,
    /**
     * The level of compression for Zlib to be applied to the artifact archive. The value can range
     * from 0 to 9: - 0: No compression - 1: Best speed - 6: Default compression (same as GNU Gzip) -
     * 9: Best compression Higher levels will result in better compression, but will take longer to
     * complete. For large files that are not easily compressed, a value of 0 is recommended for
     * significantly faster uploads.
     */
    public val compressionLevel: UploadArtifactV4.CompressionLevel? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<UploadArtifactV4.Outputs>("actions", "upload-artifact", _customVersion ?: "v4") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        name: String? = null,
        path: List<String>,
        ifNoFilesFound: UploadArtifactV4.BehaviorIfNoFilesFound? = null,
        retentionDays: UploadArtifactV4.RetentionPeriod? = null,
        compressionLevel: UploadArtifactV4.CompressionLevel? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(name=name, path=path, ifNoFilesFound=ifNoFilesFound, retentionDays=retentionDays,
            compressionLevel=compressionLevel, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            name?.let { "name" to it },
            "path" to path.joinToString("\n"),
            ifNoFilesFound?.let { "if-no-files-found" to it.stringValue },
            retentionDays?.let { "retention-days" to it.integerValue.toString() },
            compressionLevel?.let { "compression-level" to it.integerValue.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class BehaviorIfNoFilesFound(
        public val stringValue: String,
    ) {
        public object Warn : UploadArtifactV4.BehaviorIfNoFilesFound("warn")

        public object Error : UploadArtifactV4.BehaviorIfNoFilesFound("error")

        public object Ignore : UploadArtifactV4.BehaviorIfNoFilesFound("ignore")

        public class Custom(
            customStringValue: String,
        ) : UploadArtifactV4.BehaviorIfNoFilesFound(customStringValue)
    }

    public sealed class RetentionPeriod(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : UploadArtifactV4.RetentionPeriod(requestedValue)

        public object Default : UploadArtifactV4.RetentionPeriod(0)
    }

    public sealed class CompressionLevel(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : UploadArtifactV4.CompressionLevel(requestedValue)

        public object NoCompression : UploadArtifactV4.CompressionLevel(0)

        public object BestSpeed : UploadArtifactV4.CompressionLevel(1)

        public object DefaultCompression : UploadArtifactV4.CompressionLevel(6)

        public object BestCompression : UploadArtifactV4.CompressionLevel(9)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * A unique identifier for the artifact that was just uploaded. Empty if the artifact upload
         * failed.
         * This ID can be used as input to other APIs to download, delete or get more information
         * about an artifact: https://docs.github.com/en/rest/actions/artifacts
         */
        public val artifactId: String = "steps.$stepId.outputs.artifact-id"

        /**
         * A download URL for the artifact that was just uploaded. Empty if the artifact upload
         * failed.
         * This download URL only works for requests Authenticated with GitHub. Anonymous downloads
         * will be prompted to first login.  If an anonymous download URL is needed than a short time
         * restricted URL can be generated using the download artifact API:
         * https://docs.github.com/en/rest/actions/artifacts#download-an-artifact
         * This URL will be valid for as long as the artifact exists and the workflow run and
         * repository exists. Once an artifact has expired this URL will no longer work. Common uses
         * cases for such a download URL can be adding download links to artifacts in descriptions or
         * comments on pull requests or issues.
         */
        public val artifactUrl: String = "steps.$stepId.outputs.artifact-url"
    }
}
