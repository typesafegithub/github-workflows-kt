package io.github.typesafegithub.workflows

import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.testutils.JdkVersionToBytecodeVersion.JDK_11
import io.github.typesafegithub.workflows.testutils.shouldHaveBytecodeVersion
import io.kotest.core.spec.style.FunSpec

class JavaBytecodeVersionTest :
    FunSpec(
        {
            test("library is built with desired Java bytecode version") {
                Workflow::class shouldHaveBytecodeVersion JDK_11
            }
        },
    )
