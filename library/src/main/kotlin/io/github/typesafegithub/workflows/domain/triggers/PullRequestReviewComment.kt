package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#pull_request_review_comment
 */
@Serializable
public data class PullRequestReviewComment(
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()
