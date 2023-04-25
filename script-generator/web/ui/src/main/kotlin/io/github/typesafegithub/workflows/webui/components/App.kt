package io.github.typesafegithub.workflows.webui.components

import emotion.react.css
import io.github.typesafegithub.workflows.scriptgenerator.rest.api.YamlToKotlinRequest
import io.github.typesafegithub.workflows.scriptgenerator.rest.api.YamlToKotlinResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mui.material.TextField
import react.FC
import react.Props
import react.dom.html.ReactHTML.br
import react.dom.onChange
import react.useEffect
import react.useState

val App = FC<Props> {
    val (yamlToConvert, setYamlToConvert) = useState("")
    val (kotlinScript, setKotlinScript) = useState("")
    val (whichYamlConverted, setWhichYamlConverted) = useState("")

    useEffect(yamlToConvert) {
        print("useEffect")
        if (yamlToConvert != whichYamlConverted) {
            print("inside if")
            GlobalScope.launch {
                val client = HttpClient(Js) {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                val response = client.post(urlString = "/api/yaml-to-kotlin") {
                    contentType(ContentType.Application.Json)
                    setBody(YamlToKotlinRequest(yaml = yamlToConvert))
                }
                val responseBody = response.body<YamlToKotlinResponse>()
                setKotlinScript(responseBody.kotlinScript ?: responseBody.error ?: "Unknown result")
                setWhichYamlConverted(yamlToConvert)
            }
        }
    }

    +"YAML-to-Kotlin GitHub Actions workflows converter"
    br {}
    TextField {
        multiline = true
        rows = 10
        onChange = { event ->
            setYamlToConvert(event.target.asDynamic().value as String)
        }
    }
    TextField {
        multiline = true
        rows = 10
        value = kotlinScript
    }
}
