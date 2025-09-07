package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.testutils.JdkVersionToBytecodeVersion.JDK_8
import io.github.typesafegithub.workflows.testutils.shouldHaveBytecodeVersion
import io.kotest.core.spec.style.FunSpec

class JavaBytecodeVersionTest :
    FunSpec(
        {
            test("bindings are built with desired Java bytecode version") {
                val actionCoords = ActionCoords("actions", "setup-java", "v3")
                val jars = actionCoords.buildJars()!!
                jars.randomClassFile() shouldHaveBytecodeVersion JDK_8
            }
        },
    )
