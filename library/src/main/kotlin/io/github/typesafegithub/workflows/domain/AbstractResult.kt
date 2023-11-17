package io.github.typesafegithub.workflows.domain

public abstract class AbstractResult internal constructor(
    private val value: String
) {

    public fun eq(status: Status): String = "$value == $status"
    public fun neq(status: Status): String = "$value != $status"

    override fun toString(): String = value

    public enum class Status {
        Success,
        Failure,
        Cancelled,
        Skipped;

        override fun toString(): String = "'${name.lowercase()}'"
    }
}
