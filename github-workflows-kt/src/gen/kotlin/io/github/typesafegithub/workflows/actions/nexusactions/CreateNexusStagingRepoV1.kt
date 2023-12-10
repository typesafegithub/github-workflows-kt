// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.nexusactions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Create Nexus Staging Repo
 *
 * Creates a new staging repo on https://oss.sonatype.org/
 *
 * [Action on GitHub](https://github.com/nexus-actions/create-nexus-staging-repo)
 */
public data class CreateNexusStagingRepoV1 private constructor(
    /**
     * Your Sonatype username, same the Sonatype Jira one
     */
    public val username: String,
    /**
     * Your Sonatype password, same the Sonatype Jira one
     */
    public val password: String,
    /**
     * Your staging profile ID. You can get it at
     * https://oss.sonatype.org/#stagingProfiles;$staginProfileId
     */
    public val stagingProfileId: String,
    /**
     * A description to identify the newly created repository
     */
    public val description: String? = null,
    /**
     * The url of your nexus repository, defaults to OSSRH (https://oss.sonatype.org/service/local/)
     */
    public val baseUrl: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<CreateNexusStagingRepoV1.Outputs>("nexus-actions", "create-nexus-staging-repo",
        _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        username: String,
        password: String,
        stagingProfileId: String,
        description: String? = null,
        baseUrl: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(username=username, password=password, stagingProfileId=stagingProfileId,
            description=description, baseUrl=baseUrl, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "username" to username,
            "password" to password,
            "staging_profile_id" to stagingProfileId,
            description?.let { "description" to it },
            baseUrl?.let { "base_url" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * The ID of the newly created staging repository
         */
        public val repositoryId: String = "steps.$stepId.outputs.repository_id"
    }
}
