import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class CaseTest : FunSpec({

    test("Owner package should be correct") {
        val testCases = listOf(
            "actions" to "actions",
            "ORGA" to "orga",
            "My-Orga" to "myorga",
            "my_orga" to "myorga",
        )
        testCases.forAll { (owner, expected) ->
            val actual = ActionCoords(owner, "name", "v1").ownerPackage()
            actual shouldBe "it.krzeminski.githubactions.actions.$expected"
        }

    }

    test("Classnames should be correct") {
        val testCases = table(
            headers("classname", "version", "expected"),
            row("setup-node", "v12","SetupNodeV12"),
            row("setup-node", "V12","SetupNodeV12"),
            row("gradleWrapper", "2.0.0","GradleWrapperV2"),
            row("setup_node", "v2.0.0","SetupNodeV2"),
            row("my_action", "v42.0.1","MyActionV42"),
        )
        testCases.forAll { classname, version, expected ->
            val actual = ActionCoords("owner", classname, version).className()
            actual shouldBe expected
        }
    }

    test("Properties should be in camel case") {
        val testCases = listOf(
            "param" to "param",
            "my-param" to "myParam",
            "my_param" to "myParam",
            "Param" to "param",
        )
        testCases.forAll {
            camelCase(it.first) shouldBe it.second
        }
    }

    test("Property type should be guessed correctly") {
        val noDefaultInput = Input("This has no default parameter", default = null)
        val stringInput = Input("this has a string parameter", default = "some default")
        val integerInput = Input("this has an integer parameter", default = "42")
        val booleanInput = Input("this has a boolean parameter", default = "false")

        listOf(
            noDefaultInput to typeNullableString,
            stringInput to typeNullableString,
            integerInput to typeNullableInteger,
            booleanInput to typeNullableBoolean,
        ).forAll { (input, expected) ->
            input.guessPropertyType() shouldBe expected
        }
    }
})
