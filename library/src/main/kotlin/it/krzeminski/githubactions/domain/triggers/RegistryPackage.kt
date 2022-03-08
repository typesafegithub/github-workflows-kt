package it.krzeminski.githubactions.domain.triggers

/**
 * https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#registry_package
 */
data class RegistryPackage(
    override val types: List<String> = emptyList()
) : Trigger(), HasTypes
