@file:Suppress("ktlint:standard:import-ordering")

package io.github.typesafegithub.workflows.docsnippets

import io.kotest.core.spec.style.FunSpec
// --8<-- [start:getting-started-2]
import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile
// --8<-- [end:getting-started-2]
import java.io.File

class GettingStartedSnippets : FunSpec({
    test("gettingStarted") {
        /*
        // --8<-- [start:getting-started-1]
        #!/usr/bin/env kotlin

        @file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.0.0")

        // --8<-- [end:getting-started-1]
         */
        @Suppress("VariableNaming")
        val __FILE__ = File("")
        // --8<-- [start:getting-started-3]

        workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = __FILE__.toPath(),
        ) {
            job(id = "test_job", runsOn = UbuntuLatest) {
                uses(name = "Check out", action = CheckoutV3())
                run(name = "Print greeting", command = "echo 'Hello world!'")
            }
        }.writeToFile()
        // --8<-- [end:getting-started-3]
    }
})
