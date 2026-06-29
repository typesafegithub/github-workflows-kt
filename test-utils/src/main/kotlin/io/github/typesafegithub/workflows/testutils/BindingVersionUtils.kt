package io.github.typesafegithub.workflows.testutils

import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.kotest.core.spec.style.scopes.DescribeSpecContainerScope
import io.kotest.core.spec.style.scopes.DescribeSpecRootScope
import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import io.kotest.core.spec.style.scopes.FunSpecRootScope
import io.kotest.core.test.TestScope
import io.kotest.datatest.withContexts
import io.kotest.datatest.withIts
import io.kotest.datatest.withTests
import kotlin.ranges.rangeTo

fun FunSpecRootScope.withBindingVersions(
    bindingVersions: Iterable<BindingVersion>,
    test: suspend FunSpecContainerScope.(BindingVersion) -> Unit,
) = withContexts(
    nameFn = { "binding version $it" },
    ts = bindingVersions,
    test = test,
)

fun FunSpecRootScope.withAllBindingVersions(test: suspend FunSpecContainerScope.(BindingVersion) -> Unit) =
    withBindingVersions(
        bindingVersions = BindingVersion.entries,
        test = test,
    )

fun FunSpecRootScope.withBindingVersions(
    bindingVersions: OpenEndRange<BindingVersion>,
    test: suspend FunSpecContainerScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

fun FunSpecRootScope.withBindingVersions(
    bindingVersions: ClosedRange<BindingVersion>,
    test: suspend FunSpecContainerScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

fun FunSpecRootScope.withBindingVersionsFrom(
    bindingVersion: BindingVersion,
    test: suspend FunSpecContainerScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = bindingVersion..BindingVersion.entries.last(),
    test = test,
)

suspend fun FunSpecContainerScope.withBindingVersions(
    bindingVersions: Iterable<BindingVersion>,
    test: suspend TestScope.(BindingVersion) -> Unit,
) = withTests(
    nameFn = { "binding version $it" },
    ts = bindingVersions,
    test = test,
)

suspend fun FunSpecContainerScope.withAllBindingVersions(test: suspend TestScope.(BindingVersion) -> Unit) =
    withBindingVersions(
        bindingVersions = BindingVersion.entries,
        test = test,
    )

suspend fun FunSpecContainerScope.withBindingVersions(
    bindingVersions: OpenEndRange<BindingVersion>,
    test: suspend TestScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

suspend fun FunSpecContainerScope.withBindingVersions(
    bindingVersions: ClosedRange<BindingVersion>,
    test: suspend TestScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

suspend fun FunSpecContainerScope.withBindingVersionsFrom(
    bindingVersion: BindingVersion,
    test: suspend TestScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = bindingVersion..BindingVersion.entries.last(),
    test = test,
)

fun DescribeSpecRootScope.withBindingVersions(
    bindingVersions: Iterable<BindingVersion>,
    test: suspend DescribeSpecContainerScope.(BindingVersion) -> Unit,
) = withContexts(
    nameFn = { "binding version $it" },
    ts = bindingVersions,
    test = test,
)

fun DescribeSpecRootScope.withAllBindingVersions(test: suspend DescribeSpecContainerScope.(BindingVersion) -> Unit) =
    withBindingVersions(
        bindingVersions = BindingVersion.entries,
        test = test,
    )

fun DescribeSpecRootScope.withBindingVersions(
    bindingVersions: OpenEndRange<BindingVersion>,
    test: suspend DescribeSpecContainerScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

fun DescribeSpecRootScope.withBindingVersions(
    bindingVersions: ClosedRange<BindingVersion>,
    test: suspend DescribeSpecContainerScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

fun DescribeSpecRootScope.withBindingVersionsFrom(
    bindingVersion: BindingVersion,
    test: suspend DescribeSpecContainerScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = bindingVersion..BindingVersion.entries.last(),
    test = test,
)

suspend fun DescribeSpecContainerScope.withBindingVersions(
    bindingVersions: Iterable<BindingVersion>,
    test: suspend TestScope.(BindingVersion) -> Unit,
) = withIts(
    nameFn = { "binding version $it" },
    ts = bindingVersions,
    test = test,
)

suspend fun DescribeSpecContainerScope.withAllBindingVersions(test: suspend TestScope.(BindingVersion) -> Unit) =
    withBindingVersions(
        bindingVersions = BindingVersion.entries,
        test = test,
    )

suspend fun DescribeSpecContainerScope.withBindingVersions(
    bindingVersions: OpenEndRange<BindingVersion>,
    test: suspend TestScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

suspend fun DescribeSpecContainerScope.withBindingVersions(
    bindingVersions: ClosedRange<BindingVersion>,
    test: suspend TestScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

suspend fun DescribeSpecContainerScope.withBindingVersionsFrom(
    bindingVersion: BindingVersion,
    test: suspend TestScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = bindingVersion..BindingVersion.entries.last(),
    test = test,
)
