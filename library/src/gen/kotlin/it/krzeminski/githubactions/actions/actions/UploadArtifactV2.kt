// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action
import kotlin.Int
import kotlin.String
import kotlin.Suppress
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
public class UploadArtifactV2(
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
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf()
) : Action("actions", "upload-artifact", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            name?.let { "name" to it },
            "path" to path.joinToString("\n"),
            ifNoFilesFound?.let { "if-no-files-found" to it.stringValue },
            retentionDays?.let { "retention-days" to it.integerValue.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public sealed class BehaviorIfNoFilesFound(
        public val stringValue: String
    ) {
        public object Warn : UploadArtifactV2.BehaviorIfNoFilesFound("warn")

        public object Error : UploadArtifactV2.BehaviorIfNoFilesFound("error")

        public object Ignore : UploadArtifactV2.BehaviorIfNoFilesFound("ignore")

        public class Custom(
            customStringValue: String
        ) : UploadArtifactV2.BehaviorIfNoFilesFound(customStringValue)
    }

    public sealed class RetentionPeriod(
        public val integerValue: Int
    ) {
        public class Value(
            requestedValue: Int
        ) : UploadArtifactV2.RetentionPeriod(requestedValue)

        public object Default : UploadArtifactV2.RetentionPeriod(0)
    }
}
