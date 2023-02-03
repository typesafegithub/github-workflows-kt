package it.krzeminski.githubactions.dsl

public sealed class Preamble(public val content: String) {
    public class Just(content: String) : Preamble(content)
    public class WithOriginalBefore(content: String) : Preamble(content)
    public class WithOriginalAfter(content: String) : Preamble(content)
}
