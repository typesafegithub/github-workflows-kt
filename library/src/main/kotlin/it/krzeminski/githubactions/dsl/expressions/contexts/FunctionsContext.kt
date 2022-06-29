package it.krzeminski.githubactions.dsl.expressions.contexts

import kotlin.text.Typography.quote

/**
 * GitHub offers a set of built-in functions that you can use in expressions.
 * Some functions cast values to a string to perform comparisons.
 * GitHub casts data types to a string using these conversions:
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#functions
 */
@Suppress("FunctionOnlyReturningConstant", "TooManyFunctions")
open class FunctionsContext {
    private fun formatArgs(vararg args: String, quote: Boolean): String {
        fun String.maybeQuote(): String {
            val escaped = replace("'", "\\'")
            return if (quote) "'$escaped'" else this
        }
        return args.joinToString(prefix = "(", postfix = ")", transform = String::maybeQuote)
    }

    fun always() = "always()"

    fun success() = "success()"

    fun cancelled() = "cancelled()"

    fun failure() = "failure()"

    fun contains(search: String, item: String, quote: Boolean = false) =
        "contains" + formatArgs(search, item, quote = quote)

    fun startsWith(searchString: String, searchValue: String, quote: Boolean = false) =
        "startsWith" + formatArgs(searchString, searchValue, quote = quote)

    fun endsWith(searchString: String, searchValue: String, quote: Boolean = false) =
        "endsWith" + formatArgs(searchString, searchValue, quote = quote)

    fun format(vararg args: String, quote: Boolean = false): String {
        require(args.isNotEmpty()) { "Expected first arg like : format('Hello {0} {1} {2}', 'Mona', 'the', 'Octocat')" }
        return "format" + formatArgs(*args, quote = quote)
    }

    fun join(array: String, separator: String = ",", quote: Boolean = false) =
        "join" + formatArgs(array, separator, quote = quote)

    fun toJSON(value: String, quote: Boolean = false) =
        "toJSON" + formatArgs(value, quote = quote)

    fun fromJSON(value: String, quote: Boolean = false) =
        "fromJSON" + formatArgs(value, quote = quote)

    fun hashFiles(vararg paths: String, quote: Boolean = false) =
        "hashFiles" + formatArgs(*paths, quote = quote)
}
