package it.krzeminski.githubactions.yaml

import org.snakeyaml.engine.v2.api.DumpSettings
import org.snakeyaml.engine.v2.api.StreamDataWriter
import org.snakeyaml.engine.v2.common.FlowStyle
import org.snakeyaml.engine.v2.common.ScalarStyle
import org.snakeyaml.engine.v2.emitter.Emitter
import org.snakeyaml.engine.v2.events.DocumentEndEvent
import org.snakeyaml.engine.v2.events.DocumentStartEvent
import org.snakeyaml.engine.v2.events.ImplicitTuple
import org.snakeyaml.engine.v2.events.MappingEndEvent
import org.snakeyaml.engine.v2.events.MappingStartEvent
import org.snakeyaml.engine.v2.events.ScalarEvent
import org.snakeyaml.engine.v2.events.SequenceEndEvent
import org.snakeyaml.engine.v2.events.SequenceStartEvent
import org.snakeyaml.engine.v2.events.StreamEndEvent
import org.snakeyaml.engine.v2.events.StreamStartEvent
import java.io.StringWriter
import java.util.Optional

internal fun Any.toYaml(): String {
    val settings = DumpSettings.builder()
        .setWidth(Int.MAX_VALUE)
        .build()
    val writer = object : StringWriter(), StreamDataWriter {
        override fun flush() {
            // no-op
        }
    }
    val emitter = Emitter(settings, writer)
    emitter.emit(StreamStartEvent())
    emitter.emit(DocumentStartEvent(false, Optional.empty(), emptyMap()))

    this.elementToYaml(emitter)

    emitter.emit(DocumentEndEvent(false))
    emitter.emit(StreamEndEvent())
    return writer.toString()
}

private fun Any?.elementToYaml(emitter: Emitter) {
    when (this) {
        is Map<*, *> -> this.mapToYaml(emitter)
        is List<*> -> this.listToYaml(emitter)
        is String, is Int, is Float, is Boolean, null -> this.scalarToYaml(emitter)
        else -> error("Serializing $this is not supported!")
    }
}

private fun Map<*, *>.mapToYaml(emitter: Emitter) {
    emitter.emit(MappingStartEvent(Optional.empty(), Optional.empty(), true, FlowStyle.BLOCK))

    this.forEach { (key, value) ->
        // key
        emitter.emit(
            ScalarEvent(
                Optional.empty(),
                Optional.empty(),
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
    emitter.emit(SequenceStartEvent(Optional.empty(), Optional.empty(), true, FlowStyle.BLOCK))

    this.forEach { value ->
        value.elementToYaml(emitter)
    }

    emitter.emit(SequenceEndEvent())
}

private fun Any?.scalarToYaml(emitter: Emitter) {
    val scalarStyle = if (this is String && this.lines().size > 1) {
        ScalarStyle.LITERAL
    } else {
        ScalarStyle.PLAIN
    }
    emitter.emit(
        ScalarEvent(Optional.empty(), Optional.empty(), ImplicitTuple(true, true), this.toString(), scalarStyle),
    )
}
