package it.krzeminski.githubactions.scriptmodel

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml

val myYaml = Yaml(
    configuration = Yaml.default.configuration.copy(
        strictMode = false,
        polymorphismStyle = PolymorphismStyle.Property,
    ),
)
