// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.supercharge

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Int
import kotlin.String
import kotlin.Suppress
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
public class MongodbGithubActionV1(
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
    _customVersion: String? = null,
) : Action("supercharge", "mongodb-github-action", _customVersion ?: "v1") {
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
}
