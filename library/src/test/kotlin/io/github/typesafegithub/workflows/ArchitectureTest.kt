package io.github.typesafegithub.workflows

import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withModifier
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.verify.assertNot
import io.kotest.core.spec.style.FunSpec

class ArchitectureTest : FunSpec({
    test("all data classes use only immutable fields") {
        Konsist.scopeFromProduction()
            .classes()
            .withModifier(KoModifier.DATA)
            .properties(includeNested = true, includeLocal = true)
            .assertNot { it.hasValModifier }
    }
})
