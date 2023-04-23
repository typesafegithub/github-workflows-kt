package io.github.typesafegithub.workflows.webui

import io.github.typesafegithub.workflows.webui.components.App
import react.create
import react.dom.client.createRoot
import web.dom.document

fun main() {
    createRoot(document.createElement("div").also { document.body.appendChild(it) })
        .render(App.create())
}
