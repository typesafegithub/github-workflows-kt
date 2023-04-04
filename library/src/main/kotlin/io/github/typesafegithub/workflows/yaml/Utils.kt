package io.github.typesafegithub.workflows.yaml

@Suppress("UNCHECKED_CAST", "SpreadOperator")
internal fun <A : Any, B : Any> mapOfNotNullValues(vararg pairs: Pair<A, B?>): Map<A, B> =
    mapOf(*(pairs.filter { it.second != null } as List<Pair<A, B>>).toTypedArray())
