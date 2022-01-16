package it.krzeminski.githubactions.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Checkout(
    val fetchDepth: FetchDepth? = null,
) : Action(name = "actions/checkout@v2") {
    override fun toYamlArguments() = YamlCheckoutArguments(
        fetchDepth = when (fetchDepth) {
            FetchDepth.Infinite -> 0
            is FetchDepth.Quantity -> fetchDepth.value
            null -> 1
        },
    )
}

sealed interface FetchDepth {
    object Infinite : FetchDepth
    data class Quantity(val value: Int) : FetchDepth
}

@Serializable
data class YamlCheckoutArguments(
    @SerialName("fetch-depth")
    val fetchDepth: Int,
) : YamlActionArguments()
