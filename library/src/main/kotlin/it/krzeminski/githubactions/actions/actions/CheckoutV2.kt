package it.krzeminski.githubactions.actions.actions

import it.krzeminski.githubactions.actions.Action

data class CheckoutV2(
    val fetchDepth: FetchDepth? = null,
) : Action(name = "actions/checkout@v2") {
    @Suppress("SpreadOperator")
    override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            fetchDepth?.let {
                "fetch-depth" to when (fetchDepth) {
                    FetchDepth.Infinite -> "0"
                    is FetchDepth.Quantity -> fetchDepth.value.toString()
                }
            }
        ).toTypedArray()
    )
}

sealed interface FetchDepth {
    object Infinite : FetchDepth
    data class Quantity(val value: Int) : FetchDepth
}
