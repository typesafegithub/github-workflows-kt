// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actions

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Float
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Close Stale Issues
 *
 * Close issues and pull requests with no recent activity
 *
 * [Action on GitHub](https://github.com/actions/stale)
 */
public data class StaleV8 private constructor(
    /**
     * Token for the repository. Can be passed in using `{{ secrets.GITHUB_TOKEN }}`.
     */
    public val repoToken: String? = null,
    /**
     * The message to post on the issue when tagging it. If none provided, will not mark issues
     * stale.
     */
    public val staleIssueMessage: String? = null,
    /**
     * The message to post on the pull request when tagging it. If none provided, will not mark pull
     * requests stale.
     */
    public val stalePrMessage: String? = null,
    /**
     * The message to post on the issue when closing it. If none provided, will not comment when
     * closing an issue.
     */
    public val closeIssueMessage: String? = null,
    /**
     * The message to post on the pull request when closing it. If none provided, will not comment
     * when closing a pull requests.
     */
    public val closePrMessage: String? = null,
    /**
     * The number of days old an issue or a pull request can be before marking it stale. Set to -1
     * to never mark issues or pull requests as stale automatically.
     */
    public val daysBeforeStale: Float? = null,
    /**
     * The number of days old an issue can be before marking it stale. Set to -1 to never mark
     * issues as stale automatically. Override "days-before-stale" option regarding only the issues.
     */
    public val daysBeforeIssueStale: String? = null,
    /**
     * The number of days old a pull request can be before marking it stale. Set to -1 to never mark
     * pull requests as stale automatically. Override "days-before-stale" option regarding only the
     * pull requests.
     */
    public val daysBeforePrStale: StaleV8.Days? = null,
    /**
     * The number of days to wait to close an issue or a pull request after it being marked stale.
     * Set to -1 to never close stale issues or pull requests.
     */
    public val daysBeforeClose: Int? = null,
    /**
     * The number of days to wait to close an issue after it being marked stale. Set to -1 to never
     * close stale issues. Override "days-before-close" option regarding only the issues.
     */
    public val daysBeforeIssueClose: StaleV8.Days? = null,
    /**
     * The number of days to wait to close a pull request after it being marked stale. Set to -1 to
     * never close stale pull requests. Override "days-before-close" option regarding only the pull
     * requests.
     */
    public val daysBeforePrClose: StaleV8.Days? = null,
    /**
     * The label to apply when an issue is stale.
     */
    public val staleIssueLabel: String? = null,
    /**
     * The label to apply when an issue is closed.
     */
    public val closeIssueLabel: String? = null,
    /**
     * The labels that mean an issue is exempt from being marked stale. Separate multiple labels
     * with commas (eg. "label1,label2").
     */
    public val exemptIssueLabels: List<String>? = null,
    /**
     * The reason to use when closing an issue.
     */
    public val closeIssueReason: StaleV8.CloseIssueReason? = null,
    /**
     * The label to apply when a pull request is stale.
     */
    public val stalePrLabel: String? = null,
    /**
     * The label to apply when a pull request is closed.
     */
    public val closePrLabel: String? = null,
    /**
     * The labels that mean a pull request is exempt from being marked as stale. Separate multiple
     * labels with commas (eg. "label1,label2").
     */
    public val exemptPrLabels: List<String>? = null,
    /**
     * The milestones that mean an issue or a pull request is exempt from being marked as stale.
     * Separate multiple milestones with commas (eg. "milestone1,milestone2").
     */
    public val exemptMilestones: List<String>? = null,
    /**
     * The milestones that mean an issue is exempt from being marked as stale. Separate multiple
     * milestones with commas (eg. "milestone1,milestone2"). Override "exempt-milestones" option
     * regarding only the issues.
     */
    public val exemptIssueMilestones: List<String>? = null,
    /**
     * The milestones that mean a pull request is exempt from being marked as stale. Separate
     * multiple milestones with commas (eg. "milestone1,milestone2"). Override "exempt-milestones"
     * option regarding only the pull requests.
     */
    public val exemptPrMilestones: List<String>? = null,
    /**
     * Exempt all issues and pull requests with milestones from being marked as stale. Default to
     * false.
     */
    public val exemptAllMilestones: Boolean? = null,
    /**
     * Exempt all issues with milestones from being marked as stale. Override
     * "exempt-all-milestones" option regarding only the issues.
     */
    public val exemptAllIssueMilestones: Boolean? = null,
    /**
     * Exempt all pull requests with milestones from being marked as stale. Override
     * "exempt-all-milestones" option regarding only the pull requests.
     */
    public val exemptAllPrMilestones: Boolean? = null,
    /**
     * Only issues or pull requests with all of these labels are checked if stale. Defaults to ``
     * (disabled) and can be a comma-separated list of labels.
     */
    public val onlyLabels: List<String>? = null,
    /**
     * Only issues or pull requests with at least one of these labels are checked if stale. Defaults
     * to `` (disabled) and can be a comma-separated list of labels.
     */
    public val anyOfLabels: List<String>? = null,
    /**
     * Only issues with at least one of these labels are checked if stale. Defaults to `` (disabled)
     * and can be a comma-separated list of labels. Override "any-of-labels" option regarding only the
     * issues.
     */
    public val anyOfIssueLabels: List<String>? = null,
    /**
     * Only pull requests with at least one of these labels are checked if stale. Defaults to ``
     * (disabled) and can be a comma-separated list of labels. Override "any-of-labels" option
     * regarding only the pull requests.
     */
    public val anyOfPrLabels: List<String>? = null,
    /**
     * Only issues with all of these labels are checked if stale. Defaults to `[]` (disabled) and
     * can be a comma-separated list of labels. Override "only-labels" option regarding only the
     * issues.
     */
    public val onlyIssueLabels: List<String>? = null,
    /**
     * Only pull requests with all of these labels are checked if stale. Defaults to `[]` (disabled)
     * and can be a comma-separated list of labels. Override "only-labels" option regarding only the
     * pull requests.
     */
    public val onlyPrLabels: List<String>? = null,
    /**
     * The maximum number of operations per run, used to control rate limiting (GitHub API CRUD
     * related).
     */
    public val operationsPerRun: Int? = null,
    /**
     * Remove stale labels from issues and pull requests when they are updated or commented on.
     */
    public val removeStaleWhenUpdated: Boolean? = null,
    /**
     * Remove stale labels from issues when they are updated or commented on. Override
     * "remove-stale-when-updated" option regarding only the issues.
     */
    public val removeIssueStaleWhenUpdated: Boolean? = null,
    /**
     * Remove stale labels from pull requests when they are updated or commented on. Override
     * "remove-stale-when-updated" option regarding only the pull requests.
     */
    public val removePrStaleWhenUpdated: Boolean? = null,
    /**
     * Run the processor in debug mode without actually performing any operations on live issues.
     */
    public val debugOnly: Boolean? = null,
    /**
     * The order to get issues or pull requests. Defaults to false, which is descending.
     */
    public val ascending: Boolean? = null,
    /**
     * Delete the git branch after closing a stale pull request.
     */
    public val deleteBranch: Boolean? = null,
    /**
     * The date used to skip the stale action on issue/pull request created before it (ISO 8601 or
     * RFC 2822).
     */
    public val startDate: String? = null,
    /**
     * The assignees which exempt an issue or a pull request from being marked as stale. Separate
     * multiple assignees with commas (eg. "user1,user2").
     */
    public val exemptAssignees: List<String>? = null,
    /**
     * The assignees which exempt an issue from being marked as stale. Separate multiple assignees
     * with commas (eg. "user1,user2"). Override "exempt-assignees" option regarding only the issues.
     */
    public val exemptIssueAssignees: List<String>? = null,
    /**
     * The assignees which exempt a pull request from being marked as stale. Separate multiple
     * assignees with commas (eg. "user1,user2"). Override "exempt-assignees" option regarding only the
     * pull requests.
     */
    public val exemptPrAssignees: List<String>? = null,
    /**
     * Exempt all issues and pull requests with assignees from being marked as stale. Default to
     * false.
     */
    public val exemptAllAssignees: Boolean? = null,
    /**
     * Exempt all issues with assignees from being marked as stale. Override "exempt-all-assignees"
     * option regarding only the issues.
     */
    public val exemptAllIssueAssignees: String? = null,
    /**
     * Exempt all pull requests with assignees from being marked as stale. Override
     * "exempt-all-assignees" option regarding only the pull requests.
     */
    public val exemptAllPrAssignees: Boolean? = null,
    /**
     * Exempt draft pull requests from being marked as stale. Default to false.
     */
    public val exemptDraftPr: Boolean? = null,
    /**
     * Display some statistics at the end regarding the stale workflow (only when the logs are
     * enabled).
     */
    public val enableStatistics: Boolean? = null,
    /**
     * A comma delimited list of labels to add when an issue or pull request becomes unstale.
     */
    public val labelsToAddWhenUnstale: List<String>? = null,
    /**
     * A comma delimited list of labels to remove when an issue or pull request becomes stale.
     */
    public val labelsToRemoveWhenStale: List<String>? = null,
    /**
     * A comma delimited list of labels to remove when an issue or pull request becomes unstale.
     */
    public val labelsToRemoveWhenUnstale: List<String>? = null,
    /**
     * Any update (update/comment) can reset the stale idle time on the issues and pull requests.
     */
    public val ignoreUpdates: Boolean? = null,
    /**
     * Any update (update/comment) can reset the stale idle time on the issues. Override
     * "ignore-updates" option regarding only the issues.
     */
    public val ignoreIssueUpdates: Boolean? = null,
    /**
     * Any update (update/comment) can reset the stale idle time on the pull requests. Override
     * "ignore-updates" option regarding only the pull requests.
     */
    public val ignorePrUpdates: Boolean? = null,
    /**
     * Only the issues or the pull requests with an assignee will be marked as stale automatically.
     */
    public val includeOnlyAssigned: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<StaleV8.Outputs>("actions", "stale", _customVersion ?: "v8") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        repoToken: String? = null,
        staleIssueMessage: String? = null,
        stalePrMessage: String? = null,
        closeIssueMessage: String? = null,
        closePrMessage: String? = null,
        daysBeforeStale: Float? = null,
        daysBeforeIssueStale: String? = null,
        daysBeforePrStale: StaleV8.Days? = null,
        daysBeforeClose: Int? = null,
        daysBeforeIssueClose: StaleV8.Days? = null,
        daysBeforePrClose: StaleV8.Days? = null,
        staleIssueLabel: String? = null,
        closeIssueLabel: String? = null,
        exemptIssueLabels: List<String>? = null,
        closeIssueReason: StaleV8.CloseIssueReason? = null,
        stalePrLabel: String? = null,
        closePrLabel: String? = null,
        exemptPrLabels: List<String>? = null,
        exemptMilestones: List<String>? = null,
        exemptIssueMilestones: List<String>? = null,
        exemptPrMilestones: List<String>? = null,
        exemptAllMilestones: Boolean? = null,
        exemptAllIssueMilestones: Boolean? = null,
        exemptAllPrMilestones: Boolean? = null,
        onlyLabels: List<String>? = null,
        anyOfLabels: List<String>? = null,
        anyOfIssueLabels: List<String>? = null,
        anyOfPrLabels: List<String>? = null,
        onlyIssueLabels: List<String>? = null,
        onlyPrLabels: List<String>? = null,
        operationsPerRun: Int? = null,
        removeStaleWhenUpdated: Boolean? = null,
        removeIssueStaleWhenUpdated: Boolean? = null,
        removePrStaleWhenUpdated: Boolean? = null,
        debugOnly: Boolean? = null,
        ascending: Boolean? = null,
        deleteBranch: Boolean? = null,
        startDate: String? = null,
        exemptAssignees: List<String>? = null,
        exemptIssueAssignees: List<String>? = null,
        exemptPrAssignees: List<String>? = null,
        exemptAllAssignees: Boolean? = null,
        exemptAllIssueAssignees: String? = null,
        exemptAllPrAssignees: Boolean? = null,
        exemptDraftPr: Boolean? = null,
        enableStatistics: Boolean? = null,
        labelsToAddWhenUnstale: List<String>? = null,
        labelsToRemoveWhenStale: List<String>? = null,
        labelsToRemoveWhenUnstale: List<String>? = null,
        ignoreUpdates: Boolean? = null,
        ignoreIssueUpdates: Boolean? = null,
        ignorePrUpdates: Boolean? = null,
        includeOnlyAssigned: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(repoToken=repoToken, staleIssueMessage=staleIssueMessage,
            stalePrMessage=stalePrMessage, closeIssueMessage=closeIssueMessage,
            closePrMessage=closePrMessage, daysBeforeStale=daysBeforeStale,
            daysBeforeIssueStale=daysBeforeIssueStale, daysBeforePrStale=daysBeforePrStale,
            daysBeforeClose=daysBeforeClose, daysBeforeIssueClose=daysBeforeIssueClose,
            daysBeforePrClose=daysBeforePrClose, staleIssueLabel=staleIssueLabel,
            closeIssueLabel=closeIssueLabel, exemptIssueLabels=exemptIssueLabels,
            closeIssueReason=closeIssueReason, stalePrLabel=stalePrLabel, closePrLabel=closePrLabel,
            exemptPrLabels=exemptPrLabels, exemptMilestones=exemptMilestones,
            exemptIssueMilestones=exemptIssueMilestones, exemptPrMilestones=exemptPrMilestones,
            exemptAllMilestones=exemptAllMilestones,
            exemptAllIssueMilestones=exemptAllIssueMilestones,
            exemptAllPrMilestones=exemptAllPrMilestones, onlyLabels=onlyLabels,
            anyOfLabels=anyOfLabels, anyOfIssueLabels=anyOfIssueLabels, anyOfPrLabels=anyOfPrLabels,
            onlyIssueLabels=onlyIssueLabels, onlyPrLabels=onlyPrLabels,
            operationsPerRun=operationsPerRun, removeStaleWhenUpdated=removeStaleWhenUpdated,
            removeIssueStaleWhenUpdated=removeIssueStaleWhenUpdated,
            removePrStaleWhenUpdated=removePrStaleWhenUpdated, debugOnly=debugOnly,
            ascending=ascending, deleteBranch=deleteBranch, startDate=startDate,
            exemptAssignees=exemptAssignees, exemptIssueAssignees=exemptIssueAssignees,
            exemptPrAssignees=exemptPrAssignees, exemptAllAssignees=exemptAllAssignees,
            exemptAllIssueAssignees=exemptAllIssueAssignees,
            exemptAllPrAssignees=exemptAllPrAssignees, exemptDraftPr=exemptDraftPr,
            enableStatistics=enableStatistics, labelsToAddWhenUnstale=labelsToAddWhenUnstale,
            labelsToRemoveWhenStale=labelsToRemoveWhenStale,
            labelsToRemoveWhenUnstale=labelsToRemoveWhenUnstale, ignoreUpdates=ignoreUpdates,
            ignoreIssueUpdates=ignoreIssueUpdates, ignorePrUpdates=ignorePrUpdates,
            includeOnlyAssigned=includeOnlyAssigned, _customInputs=_customInputs,
            _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            repoToken?.let { "repo-token" to it },
            staleIssueMessage?.let { "stale-issue-message" to it },
            stalePrMessage?.let { "stale-pr-message" to it },
            closeIssueMessage?.let { "close-issue-message" to it },
            closePrMessage?.let { "close-pr-message" to it },
            daysBeforeStale?.let { "days-before-stale" to it.toString() },
            daysBeforeIssueStale?.let { "days-before-issue-stale" to it },
            daysBeforePrStale?.let { "days-before-pr-stale" to it.integerValue.toString() },
            daysBeforeClose?.let { "days-before-close" to it.toString() },
            daysBeforeIssueClose?.let { "days-before-issue-close" to it.integerValue.toString() },
            daysBeforePrClose?.let { "days-before-pr-close" to it.integerValue.toString() },
            staleIssueLabel?.let { "stale-issue-label" to it },
            closeIssueLabel?.let { "close-issue-label" to it },
            exemptIssueLabels?.let { "exempt-issue-labels" to it.joinToString(",") },
            closeIssueReason?.let { "close-issue-reason" to it.stringValue },
            stalePrLabel?.let { "stale-pr-label" to it },
            closePrLabel?.let { "close-pr-label" to it },
            exemptPrLabels?.let { "exempt-pr-labels" to it.joinToString(",") },
            exemptMilestones?.let { "exempt-milestones" to it.joinToString(",") },
            exemptIssueMilestones?.let { "exempt-issue-milestones" to it.joinToString(",") },
            exemptPrMilestones?.let { "exempt-pr-milestones" to it.joinToString(",") },
            exemptAllMilestones?.let { "exempt-all-milestones" to it.toString() },
            exemptAllIssueMilestones?.let { "exempt-all-issue-milestones" to it.toString() },
            exemptAllPrMilestones?.let { "exempt-all-pr-milestones" to it.toString() },
            onlyLabels?.let { "only-labels" to it.joinToString(",") },
            anyOfLabels?.let { "any-of-labels" to it.joinToString(",") },
            anyOfIssueLabels?.let { "any-of-issue-labels" to it.joinToString(",") },
            anyOfPrLabels?.let { "any-of-pr-labels" to it.joinToString(",") },
            onlyIssueLabels?.let { "only-issue-labels" to it.joinToString(",") },
            onlyPrLabels?.let { "only-pr-labels" to it.joinToString(",") },
            operationsPerRun?.let { "operations-per-run" to it.toString() },
            removeStaleWhenUpdated?.let { "remove-stale-when-updated" to it.toString() },
            removeIssueStaleWhenUpdated?.let { "remove-issue-stale-when-updated" to it.toString() },
            removePrStaleWhenUpdated?.let { "remove-pr-stale-when-updated" to it.toString() },
            debugOnly?.let { "debug-only" to it.toString() },
            ascending?.let { "ascending" to it.toString() },
            deleteBranch?.let { "delete-branch" to it.toString() },
            startDate?.let { "start-date" to it },
            exemptAssignees?.let { "exempt-assignees" to it.joinToString(",") },
            exemptIssueAssignees?.let { "exempt-issue-assignees" to it.joinToString(",") },
            exemptPrAssignees?.let { "exempt-pr-assignees" to it.joinToString(",") },
            exemptAllAssignees?.let { "exempt-all-assignees" to it.toString() },
            exemptAllIssueAssignees?.let { "exempt-all-issue-assignees" to it },
            exemptAllPrAssignees?.let { "exempt-all-pr-assignees" to it.toString() },
            exemptDraftPr?.let { "exempt-draft-pr" to it.toString() },
            enableStatistics?.let { "enable-statistics" to it.toString() },
            labelsToAddWhenUnstale?.let { "labels-to-add-when-unstale" to it.joinToString(",") },
            labelsToRemoveWhenStale?.let { "labels-to-remove-when-stale" to it.joinToString(",") },
            labelsToRemoveWhenUnstale?.let { "labels-to-remove-when-unstale" to it.joinToString(",")
                    },
            ignoreUpdates?.let { "ignore-updates" to it.toString() },
            ignoreIssueUpdates?.let { "ignore-issue-updates" to it.toString() },
            ignorePrUpdates?.let { "ignore-pr-updates" to it.toString() },
            includeOnlyAssigned?.let { "include-only-assigned" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

    public sealed class Days(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : StaleV8.Days(requestedValue)

        public object Never : StaleV8.Days(-1)
    }

    public sealed class CloseIssueReason(
        public val stringValue: String,
    ) {
        public object Completed : StaleV8.CloseIssueReason("completed")

        public object NotPlanned : StaleV8.CloseIssueReason("not_planned")

        public class Custom(
            customStringValue: String,
        ) : StaleV8.CloseIssueReason(customStringValue)
    }

    public class Outputs(
        stepId: String,
    ) : Action.Outputs(stepId) {
        /**
         * List of all closed issues and pull requests.
         */
        public val closedIssuesPrs: String = "steps.$stepId.outputs.closed-issues-prs"

        /**
         * List of all staled issues and pull requests.
         */
        public val staledIssuesPrs: String = "steps.$stepId.outputs.staled-issues-prs"
    }
}
