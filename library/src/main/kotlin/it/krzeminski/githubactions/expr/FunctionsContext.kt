package it.krzeminski.githubactions.expr

/**
 * GitHub offers a set of built-in functions that you can use in expressions.
 * Some functions cast values to a string to perform comparisons.
 * GitHub casts data types to a string using these conversions:
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#functions
 */
@Suppress("FunctionOnlyReturningConstant")
open class FunctionsContext {
    private fun formatArgs(vararg args: String) =
        args.joinToString(prefix = "(", postfix = ")") { "'$it'" }

    fun always() = "always()"

    fun contains(search: String, item: String) =
        "contains" + formatArgs(search, item)

    // TODO: other functions from https://docs.github.com/en/actions/learn-github-actions/expressions#functions
}
