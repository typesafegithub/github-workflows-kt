package io.github.typesafegithub.workflows.detekt.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassOrObject

public class NoEnumRule(
    config: Config,
) : Rule(config) {
    private companion object {
        private const val RATIONALE =
            "GitHub Actions' API may require or return values that weren't anticipated when this library was " +
                "published. Sealed interfaces/classes allow adding a class that accepts an arbitrary value as " +
                "an escape hatch."
    }

    override val issue: Issue =
        Issue(
            id = "NoEnum",
            severity = Severity.Style,
            description = "Enums are forbidden. Use sealed interfaces/classes instead. $RATIONALE",
            debt = Debt.FIVE_MINS,
        )

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        if (classOrObject is KtClass && classOrObject.isEnum()) {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(classOrObject),
                    message =
                        "Enum class '${classOrObject.name}' is forbidden. Use a sealed interface/class instead. " +
                            RATIONALE,
                ),
            )
        }
        super.visitClassOrObject(classOrObject)
    }
}
