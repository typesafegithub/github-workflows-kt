package io.github.typesafegithub.workflows.domain.contexts

import java.io.File

public object OutputsContext {
    public operator fun set(
        outputName: String,
        value: String,
    ) {
        File(System.getenv("GITHUB_OUTPUT")).appendText("$outputName=$value")
    }
}
