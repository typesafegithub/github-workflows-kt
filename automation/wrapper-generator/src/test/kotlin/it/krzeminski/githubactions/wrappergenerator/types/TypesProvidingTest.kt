package it.krzeminski.githubactions.wrappergenerator.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actionsmetadata.model.ActionCoords
import it.krzeminski.githubactions.actionsmetadata.model.BooleanTyping
import it.krzeminski.githubactions.actionsmetadata.model.EnumTyping
import it.krzeminski.githubactions.actionsmetadata.model.FloatTyping
import it.krzeminski.githubactions.actionsmetadata.model.IntegerTyping
import it.krzeminski.githubactions.actionsmetadata.model.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.actionsmetadata.model.ListOfTypings
import it.krzeminski.githubactions.actionsmetadata.model.StringTyping
import it.krzeminski.githubactions.actionsmetadata.model.TypingsSource
import it.krzeminski.githubactions.actionsmetadata.model.WrapperRequest

class TypesProvidingTest : FunSpec({
    test("valid typing provided by action") {
        // Given
        val actionTypesYml = """
            inputs:
              name:
                type: string
              verbose:
                type: boolean
              retries:
                type: integer
              some-float:
                type: float
              fetch-depth:
                type: integer
                named-values:
                  infinite: 0
              input-files:
                type: list
                separator: ','
                list-item:
                  type: string
              granted-scopes:
                type: list
                separator: ','
                list-item:
                  type: enum
                  allowed-values:
                    - read
                    - write
              permissions:
                type: enum
                allowed-values:
                  - user
                  - admin
                  - guest
        """.trimIndent()
        val wrapperRequest = WrapperRequest(
            actionCoords = ActionCoords("some-owner", "some-name", "v1"),
            typingsSource = TypingsSource.ActionTypes,
        )

        // When
        val types = wrapperRequest.provideTypes({ actionTypesYml }, { "some-hash" })

        // Then
        types shouldBe mapOf(
            "name" to StringTyping,
            "verbose" to BooleanTyping,
            "retries" to IntegerTyping,
            "some-float" to FloatTyping,
            "fetch-depth" to IntegerWithSpecialValueTyping("FetchDepth", mapOf("Infinite" to 0)),
            "input-files" to ListOfTypings(","),
            "granted-scopes" to ListOfTypings(",", EnumTyping("GrantedScopes", listOf("read", "write"))),
            "permissions" to EnumTyping("Permissions", listOf("user", "admin", "guest")),
        )
    }
},)
