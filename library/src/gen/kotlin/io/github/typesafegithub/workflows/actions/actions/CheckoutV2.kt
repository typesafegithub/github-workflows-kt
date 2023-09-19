// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
    "DEPRECATION",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Checkout
 *
 * Checkout a Git repository at a particular version
 *
 * [Action on GitHub](https://github.com/actions/checkout)
 */
@Deprecated(
    message = "This action has a newer major version: CheckoutV4",
    replaceWith = ReplaceWith("CheckoutV4"),
)
public data class CheckoutV2 private constructor(
    /**
     * Repository name with owner. For example, actions/checkout
     */
    public val repository: String? = null,
    /**
     * The branch, tag or SHA to checkout. When checking out the repository that triggered a
     * workflow, this defaults to the reference or SHA for that event.  Otherwise, uses the default
     * branch.
     */
    public val ref: String? = null,
    /**
     * Personal access token (PAT) used to fetch the repository. The PAT is configured with the
     * local git config, which enables your scripts to run authenticated git commands. The post-job
     * step removes the PAT.
     *
     * We recommend using a service account with the least permissions necessary. Also when
     * generating a new PAT, select the least scopes necessary.
     *
     * [Learn more about creating and using encrypted
     * secrets](https://help.github.com/en/actions/automating-your-workflow-with-github-actions/creating-and-using-encrypted-secrets)
     */
    public val token: String? = null,
    /**
     * SSH key used to fetch the repository. The SSH key is configured with the local git config,
     * which enables your scripts to run authenticated git commands. The post-job step removes the SSH
     * key.
     *
     * We recommend using a service account with the least permissions necessary.
     *
     * [Learn more about creating and using encrypted
     * secrets](https://help.github.com/en/actions/automating-your-workflow-with-github-actions/creating-and-using-encrypted-secrets)
     */
    public val sshKey: String? = null,
    /**
     * Known hosts in addition to the user and global host key database. The public SSH keys for a
     * host may be obtained using the utility `ssh-keyscan`. For example, `ssh-keyscan github.com`. The
     * public key for github.com is always implicitly added.
     */
    public val sshKnownHosts: String? = null,
    /**
     * Whether to perform strict host key checking. When true, adds the options
     * `StrictHostKeyChecking=yes` and `CheckHostIP=no` to the SSH command line. Use the input
     * `ssh-known-hosts` to configure additional hosts.
     */
    public val sshStrict: Boolean? = null,
    /**
     * Whether to configure the token or SSH key with the local git config
     */
    public val persistCredentials: Boolean? = null,
    /**
     * Relative path under $GITHUB_WORKSPACE to place the repository
     */
    public val path: String? = null,
    /**
     * Whether to execute `git clean -ffdx && git reset --hard HEAD` before fetching
     */
    public val clean: Boolean? = null,
    /**
     * Number of commits to fetch. 0 indicates all history for all branches and tags.
     */
    public val fetchDepth: CheckoutV2.FetchDepth? = null,
    /**
     * Whether to download Git-LFS files
     */
    public val lfs: Boolean? = null,
    /**
     * Whether to checkout submodules: `true` to checkout submodules or `recursive` to recursively
     * checkout submodules.
     *
     * When the `ssh-key` input is not provided, SSH URLs beginning with `git@github.com:` are
     * converted to HTTPS.
     */
    public val submodules: Boolean? = null,
    /**
     * Add repository path as safe.directory for Git global config by running `git
     * config --global --add safe.directory <path>`
     */
    public val setSafeDirectory: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("actions", "checkout", _customVersion ?: "v2") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        repository: String? = null,
        ref: String? = null,
        token: String? = null,
        sshKey: String? = null,
        sshKnownHosts: String? = null,
        sshStrict: Boolean? = null,
        persistCredentials: Boolean? = null,
        path: String? = null,
        clean: Boolean? = null,
        fetchDepth: CheckoutV2.FetchDepth? = null,
        lfs: Boolean? = null,
        submodules: Boolean? = null,
        setSafeDirectory: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(repository=repository, ref=ref, token=token, sshKey=sshKey,
            sshKnownHosts=sshKnownHosts, sshStrict=sshStrict, persistCredentials=persistCredentials,
            path=path, clean=clean, fetchDepth=fetchDepth, lfs=lfs, submodules=submodules,
            setSafeDirectory=setSafeDirectory, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            repository?.let { "repository" to it },
            ref?.let { "ref" to it },
            token?.let { "token" to it },
            sshKey?.let { "ssh-key" to it },
            sshKnownHosts?.let { "ssh-known-hosts" to it },
            sshStrict?.let { "ssh-strict" to it.toString() },
            persistCredentials?.let { "persist-credentials" to it.toString() },
            path?.let { "path" to it },
            clean?.let { "clean" to it.toString() },
            fetchDepth?.let { "fetch-depth" to it.integerValue.toString() },
            lfs?.let { "lfs" to it.toString() },
            submodules?.let { "submodules" to it.toString() },
            setSafeDirectory?.let { "set-safe-directory" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class FetchDepth(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : CheckoutV2.FetchDepth(requestedValue)

        public object Infinite : CheckoutV2.FetchDepth(0)
    }
}
