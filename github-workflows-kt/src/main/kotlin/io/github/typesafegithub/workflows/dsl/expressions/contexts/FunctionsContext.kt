package io.github.typesafegithub.workflows.dsl.expressions.contexts

/**
 * GitHub offers a set of built-in functions that you can use in expressions.
 * Some functions cast values to a string to perform comparisons.
 * GitHub casts data types to a string using these conversions:
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#functions
 */
@Suppress("FunctionOnlyReturningConstant", "TooManyFunctions")
public open class FunctionsContext {
    private fun formatArgs(
        vararg args: String,
        quote: Boolean,
    ): String {
        fun String.maybeQuote(): String {
            val escaped = replace("'", "\\'")
            return if (quote) "'$escaped'" else this
        }
        return args.joinToString(prefix = "(", postfix = ")", transform = String::maybeQuote)
    }

    public fun always(): String = "always()"

    public fun success(): String = "success()"

    public fun cancelled(): String = "cancelled()"

    public fun failure(): String = "failure()"

    public fun contains(
        search: String,
        item: String,
        quote: Boolean = false,
    ): String = "contains" + formatArgs(search, item, quote = quote)

    public fun startsWith(
        searchString: String,
        searchValue: String,
        quote: Boolean = false,
    ): String = "startsWith" + formatArgs(searchString, searchValue, quote = quote)

    public fun endsWith(
        searchString: String,
        searchValue: String,
        quote: Boolean = false,
    ): String = "endsWith" + formatArgs(searchString, searchValue, quote = quote)

    public fun format(
        vararg args: String,
        quote: Boolean = false,
    ): String {
        require(args.isNotEmpty()) { "Expected first arg like : format('Hello {0} {1} {2}', 'Mona', 'the', 'Octocat')" }
        return "format" + formatArgs(*args, quote = quote)
    }

    public fun join(
        array: String,
        separator: String = ",",
        quote: Boolean = false,
    ): String = "join" + formatArgs(array, separator, quote = quote)

    public fun toJSON(
        value: String,
        quote: Boolean = false,
    ): String = "toJSON" + formatArgs(value, quote = quote)

    public fun fromJSON(
        value: String,
        quote: Boolean = false,
    ): String = "fromJSON" + formatArgs(value, quote = quote)

    public fun hashFiles(
        vararg paths: String,
        quote: Boolean = false,
    ): String = "hashFiles" + formatArgs(*paths, quote = quote)
}
