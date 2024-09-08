package io.github.typesafegithub.workflows.actionbindinggenerator

import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.domain.actions.Action
import io.kotest.common.mapError
import io.kotest.core.spec.style.scopes.ContainerScope
import io.kotest.datatest.withData
import io.kotest.matchers.Matcher.Companion.failure
import io.kotest.matchers.shouldBe
import java.lang.reflect.InvocationTargetException
import java.nio.file.Paths

fun List<ActionBinding>.shouldContainAndMatchFile(path: String) {
    val binding =
        this
            .firstOrNull { it.filePath.endsWith(path) }
            ?: error("Binding with path ending with $path not found!")
    val file =
        Paths
            .get("src/test/kotlin/io/github/typesafegithub/workflows/actionbindinggenerator/bindingsfromunittests/$path")
            .toFile()
    val expectedContent =
        when {
            file.canRead() -> file.readText().removeWindowsNewLines()
            else -> ""
        }
    val actualContent = binding.kotlinCode.removeWindowsNewLines()

    binding.filePath shouldBe "io/github/typesafegithub/workflows/actions/johnsmith/$path"

    if (System.getenv("GITHUB_ACTIONS") == "true") {
        actualContent shouldBe expectedContent
    } else if (actualContent != expectedContent) {
        file.writeText(actualContent)
        actualContent shouldBe
            failure<Nothing>(
                "The binding's Kotlin code in ${file.name} doesn't match the expected one.\n" +
                    "The file has been updated to match what's expected.",
            )
    }
}

private fun String.removeWindowsNewLines(): String = replace("\r\n", "\n")

fun constructAction(
    owner: String,
    classBaseName: String,
    bindingVersion: BindingVersion,
    arguments: Map<String, Any?> = emptyMap(),
): Action<*> {
    val constructor =
        Class
            .forName("io.github.typesafegithub.workflows.actions.$owner.${classBaseName}Binding${bindingVersion.name}")
            .let {
                @Suppress("UNCHECKED_CAST")
                it as Class<Action<*>>
            }.kotlin
            .constructors
            .first()
    return runCatching {
        constructor.callBy(
            arguments.mapKeys { (key, _) ->
                constructor.parameters.first { it.name == key }
            },
        )
    }.mapError {
        if (it is InvocationTargetException) it.targetException else it
    }.getOrThrow()
}

suspend fun ContainerScope.withBindingVersions(
    bindingVersions: Iterable<BindingVersion>,
    test: suspend ContainerScope.(BindingVersion) -> Unit,
) = withData(
    nameFn = { "binding version $it" },
    ts = bindingVersions,
    test = test,
)

suspend fun ContainerScope.withAllBindingVersions(test: suspend ContainerScope.(BindingVersion) -> Unit) =
    withBindingVersions(
        bindingVersions = BindingVersion.entries,
        test = test,
    )

suspend fun ContainerScope.withBindingVersions(
    bindingVersions: OpenEndRange<BindingVersion>,
    test: suspend ContainerScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

suspend fun ContainerScope.withBindingVersions(
    bindingVersions: ClosedRange<BindingVersion>,
    test: suspend ContainerScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = BindingVersion.entries.filter { it in bindingVersions },
    test = test,
)

suspend fun ContainerScope.withBindingVersionsFrom(
    bindingVersion: BindingVersion,
    test: suspend ContainerScope.(BindingVersion) -> Unit,
) = withBindingVersions(
    bindingVersions = bindingVersion..BindingVersion.entries.last(),
    test = test,
)
