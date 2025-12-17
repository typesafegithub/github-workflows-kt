@file:Suppress("ktlint:standard:import-ordering")

package io.github.typesafegithub.workflows.docsnippets

import io.kotest.core.spec.style.FunSpec
// --8<-- [start:getting-started-2]
import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
// --8<-- [end:getting-started-2]
import java.io.File

class GettingStartedSnippets :
    FunSpec({
        test("gettingStarted") {
        /*
        // --8<-- [start:getting-started-1]
        #!/usr/bin/env kotlin

        @file:Repository("https://repo.maven.apache.org/maven2/")
        @file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.7.0")
        @file:Repository("https://bindings.krzeminski.it")
        @file:DependsOn("actions:checkout:v4")

        // --8<-- [end:getting-started-1]
         */
            @Suppress("VariableNaming", "ktlint:standard:backing-property-naming")
            val __FILE__ = File("")
            // --8<-- [start:getting-started-3]

            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = __FILE__,
            ) {
                job(id = "test_job", runsOn = UbuntuLatest) {
                    uses(name = "Check out", action = Checkout())
                    run(name = "Print greeting", command = "echo 'Hello world!'")
                }
            }
            // --8<-- [end:getting-started-3]
        }
    })
