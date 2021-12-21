package it.krzeminski.githubactions.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Checkout(
    val fetchDepth: FetchDepth,
) : Action(name = "actions/checkout@v2")

fun Action.toYamlArguments() =
    when (this) {
        is Checkout -> YamlCheckoutArguments(
            fetchDepth = when (fetchDepth) {
                FetchDepth.Infinite -> 0
                is FetchDepth.Quantity -> fetchDepth.value
            },
        )
        else -> TODO()
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
