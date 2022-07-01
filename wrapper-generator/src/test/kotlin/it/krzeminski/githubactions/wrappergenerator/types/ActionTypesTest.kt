package it.krzeminski.githubactions.wrappergenerator.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.TypingsSource
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import java.io.File

class ActionTypesTest : FunSpec({
    test("proposed 'type' attribute for 'action.yaml'") {

        StringTyping.type() shouldBe ActionType(ActionTypeEnum.String)
        IntegerTyping.type() shouldBe ActionType(ActionTypeEnum.Integer)
        BooleanTyping.type() shouldBe ActionType(ActionTypeEnum.Boolean)

        val enumType = EnumTyping("Status", listOf("success", "failure"))
        enumType.type() shouldBe ActionType(ActionTypeEnum.Enum, allowedValues = enumType.items)

        val specialTyping = IntegerWithSpecialValueTyping("FetchDepth", mapOf("Infinite" to 0))
        specialTyping.type() shouldBe ActionType(ActionTypeEnum.Integer, namedValues = specialTyping.specialValues)

        ListOfTypings(",", StringTyping).type() shouldBe ActionType(
            type = ActionTypeEnum.List,
            separator = ",",
            listItem = ActionType(ActionTypeEnum.String)
        )
        ListOfTypings(",", IntegerTyping).type() shouldBe ActionType(
            type = ActionTypeEnum.List,
            separator = ",",
            listItem = ActionType(ActionTypeEnum.Integer)
        )
        ListOfTypings(",", enumType).type() shouldBe ActionType(
            type = ActionTypeEnum.List,
            separator = ",",
            listItem = ActionType(ActionTypeEnum.Enum, allowedValues = enumType.items),
        )
    }

    test("wrapper request to YAML") {
        fun String.noWindowsEnding() = replace("\r", "")

        val request = WrapperRequest(
            ActionCoords("actions", "github-script", "v6"),
            TypingsSource.WrapperGenerator(
                mapOf(
                    "debug" to BooleanTyping,
                    "previews" to ListOfTypings(","),
                    "result-encoding" to EnumTyping("Encoding", listOf("string", "json"))
                )
            )
        )
        val resources = File("src/test/resources")
        val actionYml = resources.resolve("types/action.yml")
        val actionTypesYml = resources.resolve("types/action-types.yml")
            .readText()
            .replace("TYPING_SPEC", TYPING_SPEC)
            .noWindowsEnding()

        request.toYaml(actionYml) shouldBe actionTypesYml
    }
})
