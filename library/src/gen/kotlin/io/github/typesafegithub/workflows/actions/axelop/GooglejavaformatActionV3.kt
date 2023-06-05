// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.axelop

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
 * Action: Google Java Format
 *
 * Automatically format Java files using Google Java Style
 *
 * [Action on GitHub](https://github.com/axel-op/googlejavaformat-action)
 */
public data class GooglejavaformatActionV3 private constructor(
    /**
     * Arguments for the Google Java Format executable
     */
    public val args: String? = null,
    /**
     * Pattern to match the files to be formatted
     */
    public val files: String? = null,
    /**
     * Pattern to match the files to be ignored by this action
     */
    public val filesExcluded: String? = null,
    /**
     * By default, this action commits any change made to the files. Set to "true" to skip this
     * commit.
     */
    public val skipCommit: Boolean? = null,
    /**
     * Version of Google Java Format to use
     */
    public val version: String? = null,
    /**
     * If provided, will be used to authenticate the calls to the GitHub API.
     */
    public val githubToken: String? = null,
    /**
     * This message will be used for commits made by this action
     */
    public val commitMessage: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("axel-op", "googlejavaformat-action", _customVersion ?: "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        args: String? = null,
        files: String? = null,
        filesExcluded: String? = null,
        skipCommit: Boolean? = null,
        version: String? = null,
        githubToken: String? = null,
        commitMessage: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(args=args, files=files, filesExcluded=filesExcluded, skipCommit=skipCommit,
            version=version, githubToken=githubToken, commitMessage=commitMessage,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            args?.let { "args" to it },
            files?.let { "files" to it },
            filesExcluded?.let { "files-excluded" to it },
            skipCommit?.let { "skip-commit" to it.toString() },
            version?.let { "version" to it },
            githubToken?.let { "github-token" to it },
            commitMessage?.let { "commit-message" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
