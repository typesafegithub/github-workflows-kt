package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action

data class CheckoutV2(
    val repository: String? = null,
    val ref: String? = null,
    val token: String? = null,
    val sshKey: String? = null,
    val sshKnownHosts: String? = null,
    val sshStrict: Boolean? = null,
    val persistCredentials: Boolean? = null,
    val path: String? = null,
    val clean: Boolean? = null,
    val fetchDepth: FetchDepth? = null,
    val lfs: Boolean? = null,
    val submodules: Boolean? = null,
) : Action("actions", "checkout", "v2") {
    @Suppress("SpreadOperator", "ComplexMethod")
    override fun toYamlArguments() = linkedMapOf(
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
            fetchDepth?.let {
                "fetch-depth" to when (fetchDepth) {
                    FetchDepth.Infinite -> "0"
                    is FetchDepth.Quantity -> fetchDepth.value.toString()
                }
            },
            lfs?.let { "lfs" to it.toString() },
            submodules?.let { "submodules" to it.toString() },
        ).toTypedArray()
    )

    sealed interface FetchDepth {
        object Infinite : FetchDepth
        data class Quantity(val value: Int) : FetchDepth
    }
}
