// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.akhileshns

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress

/**
 * Action: Deploy to Heroku
 *
 * Deploy an app to Heroku
 *
 * [Action on GitHub](https://github.com/akhileshns/heroku-deploy)
 */
public class HerokuDeployV3(
    /**
     * This will be used for authentication. You can find it in your heroku homepage account
     * settings
     */
    public val herokuApiKey: String,
    /**
     * Email that you use with heroku
     */
    public val herokuEmail: String,
    /**
     * The appname to use for deploying/updating
     */
    public val herokuAppName: String,
    /**
     * An optional buildpack to use when creating the heroku application
     */
    public val buildpack: String? = null,
    /**
     * The branch that you would like to deploy to Heroku
     */
    public val branch: String? = null,
    /**
     * Set this to true if you don't want to use --force when switching branches
     */
    public val dontuseforce: Boolean? = null,
    /**
     * Set this to true if you don't want to automatically create the Heroku app
     */
    public val dontautocreate: Boolean? = null,
    /**
     * Will deploy using Dockerfile in project root.
     */
    public val usedocker: Boolean? = null,
    /**
     * Type of heroku process (web, worker, etc). This option only makes sense when usedocker
     * enabled
     */
    public val dockerHerokuProcessType: String? = null,
    /**
     * A list of args to pass into the Docker build. This option only makes sense when usedocker
     * enabled
     */
    public val dockerBuildArgs: String? = null,
    /**
     * Set if your app is located in a subdirectory.
     */
    public val appdir: String? = null,
    /**
     * A URL to which a healthcheck is performed (checks for 200 request)
     */
    public val healthcheck: String? = null,
    /**
     * Value to check for when conducting healthcheck request
     */
    public val checkstring: String? = null,
    /**
     * Time (in seconds) to wait before performing healthcheck
     */
    public val delay: Int? = null,
    /**
     * Contents of the Procfile to save and deploy
     */
    public val procfile: String? = null,
    /**
     * When set to true this will attempt to rollback to the previous release if the healthcheck
     * fails
     */
    public val rollbackonhealthcheckfailed: Boolean? = null,
    /**
     * Path to an localized env file
     */
    public val envFile: String? = null,
    /**
     * Set to true if you want the action to just login to Heroku and nothing else
     */
    public val justlogin: Boolean? = null,
    /**
     * The region in which you would like to deploy a server
     */
    public val region: String? = null,
    /**
     * Set stack of your heroku app if you need to change.Default : heroku-20
     */
    public val stack: String? = null,
    /**
     * If deploying to an organization, then specify the name of the team or organization here
     */
    public val team: String? = null
) : Action("akhileshns", "heroku-deploy", "v3.12.12") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "heroku_api_key" to herokuApiKey,
            "heroku_email" to herokuEmail,
            "heroku_app_name" to herokuAppName,
            buildpack?.let { "buildpack" to it },
            branch?.let { "branch" to it },
            dontuseforce?.let { "dontuseforce" to it.toString() },
            dontautocreate?.let { "dontautocreate" to it.toString() },
            usedocker?.let { "usedocker" to it.toString() },
            dockerHerokuProcessType?.let { "docker_heroku_process_type" to it },
            dockerBuildArgs?.let { "docker_build_args" to it },
            appdir?.let { "appdir" to it },
            healthcheck?.let { "healthcheck" to it },
            checkstring?.let { "checkstring" to it },
            delay?.let { "delay" to it.toString() },
            procfile?.let { "procfile" to it },
            rollbackonhealthcheckfailed?.let { "rollbackonhealthcheckfailed" to it.toString() },
            envFile?.let { "env_file" to it },
            justlogin?.let { "justlogin" to it.toString() },
            region?.let { "region" to it },
            stack?.let { "stack" to it },
            team?.let { "team" to it },
        ).toTypedArray()
    )
}
