package io.github.typesafegithub.workflows.domain

public abstract class AbstractResult internal constructor(
    private val value: String,
) {
    public infix fun eq(status: Status): String = "$value == $status"

    public infix fun neq(status: Status): String = "$value != $status"

    public fun toExpression(): Expression<Boolean> = Expression(value)

    override fun toString(): String = value

    public sealed class Status(
        public val name: String,
    ) {
        public object Success : Status(name = "success")

        public object Failure : Status(name = "failure")

        public object Cancelled : Status(name = "cancelled")

        public object Skipped : Status(name = "skipped")

        public data class Custom(
            val value: String,
        ) : Status(name = value)

        override fun toString(): String = "'${name.lowercase()}'"
    }
}
