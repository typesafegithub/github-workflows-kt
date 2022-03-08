package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.suggestDeprecations

class VersionSuggestionsTest : FunSpec({

    val wrapperRequests: List<WrapperRequest> = listOf(
        WrapperRequest(
            ActionCoords("owner", "name", "v4"),
        ),
        WrapperRequest(
            ActionCoords("owner", "name", "v5"),
        ),
        WrapperRequest(
            ActionCoords("owner", "other-name", "v1"),
        ),
        WrapperRequest(
            ActionCoords("owner", "other-name", "v2", deprecatedByVersion = "v3"),
        ),
        WrapperRequest(
            ActionCoords("owner", "other-name", "v3"),
        ),
    )

    test("Suggest deprecation") {
        wrapperRequests.suggestDeprecations() shouldBe """
            WARNING: newer version available for ActionCoords("owner", "name", "v4"). Maybe add: deprecatedByVersion = "v5" ?
            WARNING: newer version available for ActionCoords("owner", "other-name", "v1"). Maybe add: deprecatedByVersion = "v3" ?
        """.trimIndent()
    }
})
