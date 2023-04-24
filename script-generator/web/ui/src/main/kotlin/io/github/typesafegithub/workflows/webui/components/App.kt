package io.github.typesafegithub.workflows.webui.components

import mui.material.TextField
import react.FC
import react.Props
import react.dom.html.ReactHTML.br
import react.dom.onChange
import react.useState

val App = FC<Props> {
    val (yamlToConvert, setYamlToConvert) = useState("")
    val (kotlinScript, setKotlinScript) = useState("")
    val (whichYamlConverted, setWhichYamlConverted) = useState("")

    +"YAML-to-Kotlin GitHub Actions workflows converter"
    br {}
    TextField {
        multiline = true
        rows = 10
        onChange = { event ->
            setYamlToConvert(event.target.asDynamic().value as String)
            setKotlinScript((event.target.asDynamic().value as String).uppercase())
        }
    }
    TextField {
        multiline = true
        rows = 10
        value = kotlinScript
    }
}
