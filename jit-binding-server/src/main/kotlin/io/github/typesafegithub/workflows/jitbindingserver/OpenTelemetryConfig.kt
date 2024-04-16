package io.github.typesafegithub.workflows.jitbindingserver

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.logs.SdkLoggerProvider
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor
import io.opentelemetry.sdk.metrics.SdkMeterProvider
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import java.util.concurrent.TimeUnit

// docker run --rm \
// -e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \
// -p 16686:16686 \
// -p 4317:4317 \
// -p 4318:4318 \
// -p 9411:9411 \
// jaegertracing/all-in-one:latest
data class OpenTelemetryConfig(
    val endpointConfig: String,
    val defaultScopeName: String,
    val serviceName: String,
) {
    fun newInstance(): OpenTelemetry {
        val spanExporter =
            OtlpGrpcSpanExporter.builder()
                .setEndpoint(endpointConfig)
                .setTimeout(30, TimeUnit.SECONDS)
                .build()
//        @Suppress("DEPRECATION")
//        val spanExporter = LoggingSpanExporter()

        val metricExporter = OtlpGrpcMetricExporter.builder().build()
//        @Suppress("DEPRECATION")
//        val metricExporter = LoggingMetricExporter()

        val recordExporter = OtlpGrpcLogRecordExporter.builder().build()
//        val recordExporter = SystemOutLogRecordExporter.create()

        val resource =
            Resource.getDefault()
                .toBuilder()
                .put(AttributeKey.stringKey("service.name"), serviceName)
                .build()

        val tracerProvider =
            SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .setResource(resource)
                .build()

        val meterProvider =
            SdkMeterProvider.builder()
                .registerMetricReader(
                    PeriodicMetricReader.builder(metricExporter).build(),
                )
                .setResource(resource)
                .build()

        val loggerProvider =
            SdkLoggerProvider.builder()
                .addLogRecordProcessor(
                    BatchLogRecordProcessor.builder(recordExporter).build(),
                )
                .setResource(resource)
                .build()

        val openTelemetry =
            OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setMeterProvider(meterProvider)
                .setLoggerProvider(loggerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal()

        Runtime.getRuntime().addShutdownHook(Thread { openTelemetry.close() })
        return openTelemetry
    }

    companion object {
        fun config(serviceName: String) =
            OpenTelemetryConfig(
                endpointConfig = "http://localhost:4317",
                defaultScopeName = "com.sample",
                serviceName = serviceName,
            )
    }
}
