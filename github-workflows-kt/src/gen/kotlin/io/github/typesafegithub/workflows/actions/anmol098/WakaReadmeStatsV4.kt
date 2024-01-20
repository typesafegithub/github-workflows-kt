// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.anmol098

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Profile Readme Development Stats
 *
 * Are you an early 🐤 or a night 🦉? When are you most productive during the day? Let's check out
 * in your readme!
 *
 * [Action on GitHub](https://github.com/anmol098/waka-readme-stats)
 *
 * @param ghToken GitHub access token with Repo scope
 * @param wakatimeApiKey Your Wakatime API Key
 * @param showOs Show the list of OS Worked on In dev metrics
 * @param showProjects Show the list of projects worked on in dev metrics
 * @param showEditors Show the Editors used in dev metrics
 * @param showTimezone Show the time zone in the dev metrics
 * @param showCommit Shows the number of commit graph in the dev metrics
 * @param showLanguage Show the Coding language used in dev metrics
 * @param showLinesOfCode Show the Total Lines of code written Badge till date
 * @param showLanguagePerRepo Show language or framework used across different repository
 * @param showDaysOfWeek show day of week you are most productive
 * @param showProfileViews Shows the current profile views
 * @param showShortInfo Shows the short facts
 * @param locale Show stats in your own language
 * @param commitByMe Git commit with your own name and email
 * @param ignoredRepos Repos you don't want to be counted
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by
 * the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor
 * version, or a newer version that the binding doesn't yet know about
 */
public data class WakaReadmeStatsV4 private constructor(
    /**
     * GitHub access token with Repo scope
     */
    public val ghToken: String? = null,
    /**
     * Your Wakatime API Key
     */
    public val wakatimeApiKey: String,
    /**
     * Show the list of OS Worked on In dev metrics
     */
    public val showOs: Boolean? = null,
    /**
     * Show the list of projects worked on in dev metrics
     */
    public val showProjects: Boolean? = null,
    /**
     * Show the Editors used in dev metrics
     */
    public val showEditors: Boolean? = null,
    /**
     * Show the time zone in the dev metrics
     */
    public val showTimezone: Boolean? = null,
    /**
     * Shows the number of commit graph in the dev metrics
     */
    public val showCommit: Boolean? = null,
    /**
     * Show the Coding language used in dev metrics
     */
    public val showLanguage: Boolean? = null,
    /**
     * Show the Total Lines of code written Badge till date
     */
    public val showLinesOfCode: Boolean? = null,
    /**
     * Show language or framework used across different repository
     */
    public val showLanguagePerRepo: Boolean? = null,
    public val showLocChart: Boolean? = null,
    /**
     * show day of week you are most productive
     */
    public val showDaysOfWeek: Boolean? = null,
    /**
     * Shows the current profile views
     */
    public val showProfileViews: Boolean? = null,
    /**
     * Shows the short facts
     */
    public val showShortInfo: Boolean? = null,
    /**
     * Show stats in your own language
     */
    public val locale: String? = null,
    /**
     * Git commit with your own name and email
     */
    public val commitByMe: Boolean? = null,
    /**
     * Repos you don't want to be counted
     */
    public val ignoredRepos: List<String>? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("anmol098", "waka-readme-stats", _customVersion ?: "v4") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        ghToken: String? = null,
        wakatimeApiKey: String,
        showOs: Boolean? = null,
        showProjects: Boolean? = null,
        showEditors: Boolean? = null,
        showTimezone: Boolean? = null,
        showCommit: Boolean? = null,
        showLanguage: Boolean? = null,
        showLinesOfCode: Boolean? = null,
        showLanguagePerRepo: Boolean? = null,
        showLocChart: Boolean? = null,
        showDaysOfWeek: Boolean? = null,
        showProfileViews: Boolean? = null,
        showShortInfo: Boolean? = null,
        locale: String? = null,
        commitByMe: Boolean? = null,
        ignoredRepos: List<String>? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(ghToken=ghToken, wakatimeApiKey=wakatimeApiKey, showOs=showOs,
            showProjects=showProjects, showEditors=showEditors, showTimezone=showTimezone,
            showCommit=showCommit, showLanguage=showLanguage, showLinesOfCode=showLinesOfCode,
            showLanguagePerRepo=showLanguagePerRepo, showLocChart=showLocChart,
            showDaysOfWeek=showDaysOfWeek, showProfileViews=showProfileViews,
            showShortInfo=showShortInfo, locale=locale, commitByMe=commitByMe,
            ignoredRepos=ignoredRepos, _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            ghToken?.let { "GH_TOKEN" to it },
            "WAKATIME_API_KEY" to wakatimeApiKey,
            showOs?.let { "SHOW_OS" to it.toString() },
            showProjects?.let { "SHOW_PROJECTS" to it.toString() },
            showEditors?.let { "SHOW_EDITORS" to it.toString() },
            showTimezone?.let { "SHOW_TIMEZONE" to it.toString() },
            showCommit?.let { "SHOW_COMMIT" to it.toString() },
            showLanguage?.let { "SHOW_LANGUAGE" to it.toString() },
            showLinesOfCode?.let { "SHOW_LINES_OF_CODE" to it.toString() },
            showLanguagePerRepo?.let { "SHOW_LANGUAGE_PER_REPO" to it.toString() },
            showLocChart?.let { "SHOW_LOC_CHART" to it.toString() },
            showDaysOfWeek?.let { "SHOW_DAYS_OF_WEEK" to it.toString() },
            showProfileViews?.let { "SHOW_PROFILE_VIEWS" to it.toString() },
            showShortInfo?.let { "SHOW_SHORT_INFO" to it.toString() },
            locale?.let { "LOCALE" to it },
            commitByMe?.let { "COMMIT_BY_ME" to it.toString() },
            ignoredRepos?.let { "IGNORED_REPOS" to it.joinToString(",") },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
