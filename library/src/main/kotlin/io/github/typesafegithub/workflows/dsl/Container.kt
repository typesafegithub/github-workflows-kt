package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.domain.PortMapping
import io.github.typesafegithub.workflows.domain.PortMapping.Protocol
import io.github.typesafegithub.workflows.domain.VolumeMapping

public fun port(
    mapping: Pair<Int, Int>,
    protocol: Protocol = Protocol.All,
): PortMapping = PortMapping(host = mapping.first, container = mapping.second, protocol = protocol)

public fun port(
    host: Int,
    protocol: Protocol = Protocol.All,
): PortMapping = PortMapping(host = host, protocol = protocol)

public fun tcp(mapping: Pair<Int, Int>): PortMapping = port(mapping, Protocol.TCP)

public fun tcp(host: Int): PortMapping = port(host, Protocol.TCP)

public fun udp(mapping: Pair<Int, Int>): PortMapping = port(mapping, Protocol.UDP)

public fun udp(host: Int): PortMapping = port(host, Protocol.UDP)

public fun volume(
    mapping: Pair<String, String>,
    isReadOnly: Boolean = false,
): VolumeMapping = VolumeMapping(source = mapping.first, target = mapping.second, isReadOnly = isReadOnly)

public fun volume(
    target: String,
    isReadOnly: Boolean = false,
): VolumeMapping = VolumeMapping(target = target, isReadOnly = isReadOnly)
