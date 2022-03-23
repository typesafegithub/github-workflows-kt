package it.krzeminski.githubactions.internal

/**
 * Marks declarations that are **internal** to github-actions-kotlin-dsl,
 * which means they should not be used outside github-actions-kotlin-dsl modules,
 * because their signatures and semantics can **(will) change** between future releases,
 * without any warnings and without providing any migration aids.
 */
@RequiresOptIn
annotation class InternalGithubActionsApi
