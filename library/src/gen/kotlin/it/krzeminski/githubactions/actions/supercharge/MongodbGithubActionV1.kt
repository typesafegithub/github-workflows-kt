// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package it.krzeminski.githubactions.actions.supercharge

import it.krzeminski.githubactions.domain.actions.Action
import it.krzeminski.githubactions.domain.actions.Action.Outputs
import java.util.LinkedHashMap
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: MongoDB in GitHub Actions
 *
 * Start a MongoDB server (on default port 27017 or a custom port)
 *
 * [Action on GitHub](https://github.com/supercharge/mongodb-github-action)
 */
public data class MongodbGithubActionV1 private constructor(
    /**
     * MongoDB version to use (default "latest")
     */
    public val mongodbVersion: String? = null,
    /**
     * MongoDB replica set name (no replica set by default)
     */
    public val mongodbReplicaSet: String? = null,
    /**
     * MongoDB port to use (default 27017)
     */
    public val mongodbPort: Int? = null,
    /**
     * MongoDB db to create (default: none)
     */
    public val mongodbDb: String? = null,
    /**
     * MongoDB root username (default: none)
     */
    public val mongodbUsername: String? = null,
    /**
     * MongoDB root password (default: none)
     */
    public val mongodbPassword: String? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : Action<Action.Outputs>("supercharge", "mongodb-github-action", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        mongodbVersion: String? = null,
        mongodbReplicaSet: String? = null,
        mongodbPort: Int? = null,
        mongodbDb: String? = null,
        mongodbUsername: String? = null,
        mongodbPassword: String? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(mongodbVersion=mongodbVersion, mongodbReplicaSet=mongodbReplicaSet,
            mongodbPort=mongodbPort, mongodbDb=mongodbDb, mongodbUsername=mongodbUsername,
            mongodbPassword=mongodbPassword, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            mongodbVersion?.let { "mongodb-version" to it },
            mongodbReplicaSet?.let { "mongodb-replica-set" to it },
            mongodbPort?.let { "mongodb-port" to it.toString() },
            mongodbDb?.let { "mongodb-db" to it },
            mongodbUsername?.let { "mongodb-username" to it },
            mongodbPassword?.let { "mongodb-password" to it },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
