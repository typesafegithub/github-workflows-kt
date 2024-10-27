import io.github.typesafegithub.workflows.actions.actions.Cache
import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.Checkout_Untyped
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.actions.typesafegithub.AlwaysUntypedActionForTests_Untyped

fun main() {
    println(Checkout_Untyped(fetchTags_Untyped = "false"))
    println(Checkout(fetchTags = false))
    println(Checkout(fetchTags_Untyped = "false"))
    println(AlwaysUntypedActionForTests_Untyped(foobar_Untyped = "baz"))
    println(ActionsSetupGradle())
    println(Cache(path = listOf("some-path"), key = "some-key"))
}
