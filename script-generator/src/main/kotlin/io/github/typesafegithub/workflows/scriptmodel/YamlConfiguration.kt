package io.github.typesafegithub.workflows.scriptmodel

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml

val myYaml = Yaml(
    configuration = Yaml.default.configuration.copy(
        strictMode = false,
        polymorphismStyle = PolymorphismStyle.Property,
    ),
)
