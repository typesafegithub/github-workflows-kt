package it.krzeminski.githubactions.internal

/**
 * Marks declarations that are **internal** to github-workflows-kt,
 * which means they should not be used outside github-workflows-kt modules,
 * because their signatures and semantics can **(will) change** between future releases,
 * without any warnings and without providing any migration aids.
 */
@RequiresOptIn
public annotation class InternalGithubActionsApi
