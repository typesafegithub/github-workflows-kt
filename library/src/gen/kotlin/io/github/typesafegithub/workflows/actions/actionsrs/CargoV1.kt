// This file was generated using 'code-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.actionsrs

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: rust-cargo
 *
 * Run cargo command
 *
 * [Action on GitHub](https://github.com/actions-rs/cargo)
 */
public data class CargoV1 private constructor(
    /**
     * Cargo command to run (ex. `check` or `build`)
     */
    public val command: CargoV1.Command,
    /**
     * Toolchain to use (without the `+` sign, ex. `nightly`)
     */
    public val toolchain: String? = null,
    /**
     * Arguments for the cargo command
     */
    public val args: List<String>? = null,
    /**
     * Use cross instead of cargo
     */
    public val useCross: Boolean? = null,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the wrapper doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("actions-rs", "cargo", _customVersion ?: "v1") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        command: CargoV1.Command,
        toolchain: String? = null,
        args: List<String>? = null,
        useCross: Boolean? = null,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(command=command, toolchain=toolchain, args=args, useCross=useCross,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    public override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "command" to command.stringValue,
            toolchain?.let { "toolchain" to it },
            args?.let { "args" to it.joinToString(" ") },
            useCross?.let { "use-cross" to it.toString() },
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    public override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Command(
        public val stringValue: String,
    ) {
        public object Help : CargoV1.Command("help")

        public object Version : CargoV1.Command("version")

        public object Bench : CargoV1.Command("bench")

        public object Build : CargoV1.Command("build")

        public object Check : CargoV1.Command("check")

        public object Clean : CargoV1.Command("clean")

        public object Doc : CargoV1.Command("doc")

        public object Fetch : CargoV1.Command("fetch")

        public object Fix : CargoV1.Command("fix")

        public object Run : CargoV1.Command("run")

        public object Rustc : CargoV1.Command("rustc")

        public object Rustdoc : CargoV1.Command("rustdoc")

        public object Test : CargoV1.Command("test")

        public object Report : CargoV1.Command("report")

        public object GenerateLockfile : CargoV1.Command("generate-lockfile")

        public object LocateProject : CargoV1.Command("locate-project")

        public object Metadata : CargoV1.Command("metadata")

        public object Pkgid : CargoV1.Command("pkgid")

        public object Tree : CargoV1.Command("tree")

        public object Update : CargoV1.Command("update")

        public object Vendor : CargoV1.Command("vendor")

        public object VerifyProject : CargoV1.Command("verify-project")

        public object Init : CargoV1.Command("init")

        public object Install : CargoV1.Command("install")

        public object New : CargoV1.Command("new")

        public object Search : CargoV1.Command("search")

        public object Uninstall : CargoV1.Command("uninstall")

        public object Login : CargoV1.Command("login")

        public object Owner : CargoV1.Command("owner")

        public object Package : CargoV1.Command("package")

        public object Publish : CargoV1.Command("publish")

        public object Yank : CargoV1.Command("yank")

        public class Custom(
            customStringValue: String,
        ) : CargoV1.Command(customStringValue)
    }
}
