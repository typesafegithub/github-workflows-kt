package io.github.typesafegithub.workflows.actionbindinggenerator.utils

import com.charleskorn.kaml.Yaml

internal val myYaml =
    Yaml(
        configuration =
            Yaml.default.configuration.copy(
                strictMode = false,
            ),
    )
