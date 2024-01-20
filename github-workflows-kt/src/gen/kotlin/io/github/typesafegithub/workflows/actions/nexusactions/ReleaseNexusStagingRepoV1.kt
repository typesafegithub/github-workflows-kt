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
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Close and Release Nexus Staged Repo
 *
 * Closes and releases a staged repo, with a given ID, on https://oss.sonatype.org/.
 *
 * [Action on GitHub](https://github.com/nexus-actions/release-nexus-staging-repo)
 *
 * @param username Your Sonatype username, same the Sonatype Jira one.
 * @param password Your Sonatype password, same the Sonatype Jira one.
 * @param stagingRepositoryId The ID of the staged repository to close and release.
 * @param description The description to use for the closed repository
 * @param baseUrl The url of your nexus repository, defaults to OSSRH
 * (https://oss.sonatype.org/service/local/)
 * @param closeOnly This option disable the auto-release process, you will have to do it manually on
 * https://oss.sonatype.org/.
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class ReleaseNexusStagingRepoV1 private constructor(
    /**
     * Your Sonatype username, same the Sonatype Jira one.
     */
    public val username: String,
    /**
     * Your Sonatype password, same the Sonatype Jira one.
     */
    public val password: String,
    /**
     * The ID of the staged repository to close and release.
     */
    public val stagingRepositoryId: String,
    /**
     * The description to use for the closed repository
     */
    public val description: String? = null,
    /**
     * The url of your nexus repository, defaults to OSSRH (https://oss.sonatype.org/service/local/)
     */
    public val baseUrl: String? = null,
    /**
     * This option disable the auto-release process, you will have to do it manually on
     * https://oss.sonatype.org/.
     */
    public val closeOnly: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("nexus-actions", "release-nexus-staging-repo", _customVersion ?:
        "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        username: String,
        password: String,
        stagingRepositoryId: String,
        description: String? = null,
        baseUrl: String? = null,
        closeOnly: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(username=username, password=password, stagingRepositoryId=stagingRepositoryId,
            description=description, baseUrl=baseUrl, closeOnly=closeOnly,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "username" to username,
            "password" to password,
            "staging_repository_id" to stagingRepositoryId,
            description?.let { "description" to it },
            baseUrl?.let { "base_url" to it },
            closeOnly?.let { "close_only" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
