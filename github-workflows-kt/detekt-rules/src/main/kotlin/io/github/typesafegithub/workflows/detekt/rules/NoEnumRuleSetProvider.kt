package io.github.typesafegithub.workflows.detekt.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

public class NoEnumRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "custom-style"

    override fun instance(config: Config): RuleSet =
        RuleSet(
            id = ruleSetId,
            rules = listOf(NoEnumRule(config)),
        )
}
