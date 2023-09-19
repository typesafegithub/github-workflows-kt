package io.github.typesafegithub.workflows.actionsmetadata

import io.github.typesafegithub.workflows.actionsmetadata.model.ActionBindingRequest
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionCoords
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AddDeprecationInfoTest : FunSpec({

    test("add deprecation info") {
        listOf(
            ActionBindingRequest(ActionCoords("owner", "name", "v4")),
            ActionBindingRequest(ActionCoords("owner", "name", "v5")),
            ActionBindingRequest(ActionCoords("owner", "other-name", "v1")),
            ActionBindingRequest(ActionCoords("owner", "other-name", "v2")),
            ActionBindingRequest(ActionCoords("owner", "other-name", "v3")),
        ).addDeprecationInfo() shouldBe listOf(
            ActionBindingRequest(ActionCoords("owner", "name", "v4", deprecatedByVersion = "v5")),
            ActionBindingRequest(ActionCoords("owner", "name", "v5")),
            ActionBindingRequest(ActionCoords("owner", "other-name", "v1", deprecatedByVersion = "v3")),
            ActionBindingRequest(ActionCoords("owner", "other-name", "v2", deprecatedByVersion = "v3")),
            ActionBindingRequest(ActionCoords("owner", "other-name", "v3")),
        )
    }
})
