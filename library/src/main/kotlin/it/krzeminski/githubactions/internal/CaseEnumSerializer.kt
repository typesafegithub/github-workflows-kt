@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)

package it.krzeminski.githubactions.internal

import it.krzeminski.githubactions.yaml.snakeCaseOf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Copy/pasted from the kotlinx.serialization library
 * What changed is that the enum value is the PascalCase version of its snake_case
 */
@InternalSerializationApi
internal open class CaseEnumSerializer<T : Enum<T>>(
    serialName: String,
    private val values: Array<T>,
) : KSerializer<T> {

    override val descriptor: SerialDescriptor = buildSerialDescriptor(serialName, SerialKind.ENUM) {
        values.forEach {
            val fqn = "$serialName.${it.name}"
            val enumMemberDescriptor = buildSerialDescriptor(fqn, StructureKind.OBJECT)
            element(snakeCaseOf(it.name), enumMemberDescriptor)
        }
    }

    override fun serialize(encoder: Encoder, value: T) {
        val index = values.indexOf(value)
        if (index == -1) {
            throw SerializationException(
                "$value is not a valid enum ${descriptor.serialName}, " +
                    "must be one of ${values.contentToString()}",
            )
        }
        encoder.encodeEnum(descriptor, index)
    }

    override fun deserialize(decoder: Decoder): T {
        val index = decoder.decodeEnum(descriptor)
        if (index !in values.indices) {
            throw SerializationException(
                "$index is not among valid ${descriptor.serialName} enum values, " +
                    "values size is ${values.size}",
            )
        }
        return values[index]
    }

    override fun toString(): String = "kotlinx.serialization.CaseEnumSerializer<${descriptor.serialName}>"
}
