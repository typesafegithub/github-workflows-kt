// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actionsrs

import it.krzeminski.githubactions.actions.Action
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

/**
 * Action: rust-cargo
 *
 * Run cargo command
 *
 * [Action on GitHub](https://github.com/actions-rs/cargo)
 */
public class CargoV1(
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
    public val useCross: Boolean? = null
) : Action("actions-rs", "cargo", "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "command" to command.stringValue,
            toolchain?.let { "toolchain" to it },
            args?.let { "args" to it.joinToString(" ") },
            useCross?.let { "use-cross" to it.toString() },
        ).toTypedArray()
    )

    public sealed class Command(
        public val stringValue: String
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
            customStringValue: String
        ) : CargoV1.Command(customStringValue)
    }
}
