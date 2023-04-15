package io.github.typesafegithub.workflows.scriptgenerator

import java.io.File
import java.net.URL

fun main(args: Array<String>) {
    val arg = args.firstOrNull() ?: usage()
    val (filename, content) = when {
        arg.startsWith("http") -> URL(arg).run { filename() to readText() }
        File(arg).canRead() -> File(arg).run { nameWithoutExtension to readText() }
        else -> usage()
    }

    val kotlinScriptContents = yamlToKotlinScript(yaml = content, filename = filename)
    val buildFile = File("$filename.main.kts")
    buildFile.writeText(kotlinScriptContents)
    buildFile.setExecutable(true)
    println(
        """
        Kotlin script written to $buildFile
        Run it with:
          ./$buildFile
        The resulting YAML file will be available at $filename.yaml
        """.trimIndent(),
    )
}

fun usage(): Nothing {
    error(
        """|
           |Usage:
           |   ./gradlew :script-generator:cli:run --args /path/to/.github/workflows/build.yml
           |   ./gradlew :script-generator:cli:run --args https://raw.githubusercontent.com/krzema12/github-actions-kotlin-dsl/0f41e3322a3e7de4199000fae54b398380eace2f/.github/workflows/build.yaml
           |   ./gradlew :script-generator:cli:run --args https://gist.githubusercontent.com/jmfayard/dba8b5195292cac0e5f83c42de7cc3c2/raw/ca6143d70a8a34eea5ea64871f87cfec69443ab1/build.yml
        """.trimMargin(),
    )
}
