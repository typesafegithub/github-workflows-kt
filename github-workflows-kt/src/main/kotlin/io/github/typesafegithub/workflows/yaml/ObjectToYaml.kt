package io.github.typesafegithub.workflows.yaml

import it.krzeminski.snakeyaml.engine.kmp.api.DumpSettings
import it.krzeminski.snakeyaml.engine.kmp.api.StreamDataWriter
import it.krzeminski.snakeyaml.engine.kmp.common.FlowStyle
import it.krzeminski.snakeyaml.engine.kmp.common.ScalarStyle
import it.krzeminski.snakeyaml.engine.kmp.emitter.Emitter
import it.krzeminski.snakeyaml.engine.kmp.events.DocumentEndEvent
import it.krzeminski.snakeyaml.engine.kmp.events.DocumentStartEvent
import it.krzeminski.snakeyaml.engine.kmp.events.ImplicitTuple
import it.krzeminski.snakeyaml.engine.kmp.events.MappingEndEvent
import it.krzeminski.snakeyaml.engine.kmp.events.MappingStartEvent
import it.krzeminski.snakeyaml.engine.kmp.events.ScalarEvent
import it.krzeminski.snakeyaml.engine.kmp.events.SequenceEndEvent
import it.krzeminski.snakeyaml.engine.kmp.events.SequenceStartEvent
import it.krzeminski.snakeyaml.engine.kmp.events.StreamEndEvent
import it.krzeminski.snakeyaml.engine.kmp.events.StreamStartEvent
import java.io.StringWriter

internal fun Any.toYaml(): String {
    val settings =
        DumpSettings
            .builder()
            .build()
    val writer =
        object : StringWriter(), StreamDataWriter {
            override fun flush() {
                // no-op
            }
        }
    val emitter = Emitter(settings, writer)
    emitter.emit(StreamStartEvent())
    emitter.emit(DocumentStartEvent(false, null, emptyMap()))

    this.elementToYaml(emitter)

    emitter.emit(DocumentEndEvent(false))
    emitter.emit(StreamEndEvent())
    return writer.toString()
}

private fun Any?.elementToYaml(emitter: Emitter) {
    when (this) {
        is Map<*, *> -> this.mapToYaml(emitter)
        is List<*> -> this.listToYaml(emitter)
        is String, is Int, is Float, is Double, is Boolean, null -> this.scalarToYaml(emitter)
        else -> error("Serializing $this is not supported!")
    }
}

private fun Map<*, *>.mapToYaml(emitter: Emitter) {
    emitter.emit(MappingStartEvent(null, null, true, FlowStyle.BLOCK))

    this.forEach { (key, value) ->
        // key
        emitter.emit(
            ScalarEvent(
                null,
                null,
                ImplicitTuple(true, true),
                key.toString(),
                ScalarStyle.PLAIN,
            ),
        )
        // value
        value.elementToYaml(emitter)
    }

    emitter.emit(MappingEndEvent())
}

private fun List<*>.listToYaml(emitter: Emitter) {
    emitter.emit(SequenceStartEvent(null, null, true, FlowStyle.BLOCK))

    this.forEach { value ->
        value.elementToYaml(emitter)
    }

    emitter.emit(SequenceEndEvent())
}

private fun Any?.scalarToYaml(emitter: Emitter) {
    val scalarStyle =
        if (this is String) {
            if (lines().size > 1) {
                ScalarStyle.LITERAL
            } else {
                ScalarStyle.SINGLE_QUOTED
            }
        } else {
            ScalarStyle.PLAIN
        }
    emitter.emit(
        ScalarEvent(null, null, ImplicitTuple(true, true), this.toString(), scalarStyle),
    )
}
