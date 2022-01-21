package it.krzeminski.githubactions.actions

data class Checkout(
    val fetchDepth: FetchDepth? = null,
) : Action(name = "actions/checkout@v2") {
    override fun toYamlArguments() = linkedMapOf(
        "fetch-depth" to when (fetchDepth) {
            FetchDepth.Infinite -> "0"
            is FetchDepth.Quantity -> fetchDepth.value.toString()
            null -> "1"
        }
    )
}

sealed interface FetchDepth {
    object Infinite : FetchDepth
    data class Quantity(val value: Int) : FetchDepth
}
