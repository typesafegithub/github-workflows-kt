// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.github

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
 * Action: CodeQL: Autobuild
 *
 * Attempt to automatically build code
 *
 * [Action on GitHub](https://github.com/github/codeql-action/tree/v2/autobuild)
 *
 * @param workingDirectory Run the autobuilder using this path (relative to $GITHUB_WORKSPACE) as
 * working directory. If this input is not set, the autobuilder runs with $GITHUB_WORKSPACE as its
 * working directory.
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class CodeqlActionAutobuildV2 private constructor(
    public val token: String? = null,
    public val matrix: String? = null,
    /**
     * Run the autobuilder using this path (relative to $GITHUB_WORKSPACE) as working directory. If
     * this input is not set, the autobuilder runs with $GITHUB_WORKSPACE as its working directory.
     */
    public val workingDirectory: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("github", "codeql-action/autobuild", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        token: String? = null,
        matrix: String? = null,
        workingDirectory: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(token=token, matrix=matrix, workingDirectory=workingDirectory,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            token?.let { "token" to it },
            matrix?.let { "matrix" to it },
            workingDirectory?.let { "working-directory" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
