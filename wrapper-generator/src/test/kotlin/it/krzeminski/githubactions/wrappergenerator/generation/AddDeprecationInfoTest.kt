package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.wrappergenerator.addDeprecationInfo
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest

class AddDeprecationInfoTest : FunSpec({

    test("add deprecation info") {
        listOf(
            WrapperRequest(ActionCoords("owner", "name", "v4")),
            WrapperRequest(ActionCoords("owner", "name", "v5")),
            WrapperRequest(ActionCoords("owner", "other-name", "v1")),
            WrapperRequest(ActionCoords("owner", "other-name", "v2")),
            WrapperRequest(ActionCoords("owner", "other-name", "v3")),
        ).addDeprecationInfo() shouldBe listOf(
            WrapperRequest(ActionCoords("owner", "name", "v4", deprecatedByVersion = "v5")),
            WrapperRequest(ActionCoords("owner", "name", "v5")),
            WrapperRequest(ActionCoords("owner", "other-name", "v1", deprecatedByVersion = "v3")),
            WrapperRequest(ActionCoords("owner", "other-name", "v2", deprecatedByVersion = "v3")),
            WrapperRequest(ActionCoords("owner", "other-name", "v3")),
        )
    }
},)
