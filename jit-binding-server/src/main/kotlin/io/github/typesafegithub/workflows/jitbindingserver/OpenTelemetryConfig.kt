package io.github.typesafegithub.workflows.jitbindingserver

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.metrics.DoubleHistogramBuilder
import io.opentelemetry.api.metrics.LongCounterBuilder
import io.opentelemetry.api.metrics.Meter
import io.opentelemetry.api.metrics.MeterBuilder
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanBuilder
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.common.CompletableResultCode
import io.opentelemetry.sdk.logs.SdkLoggerProvider
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor
import io.opentelemetry.sdk.metrics.SdkMeterProvider
import io.opentelemetry.sdk.metrics.data.MetricData
import io.opentelemetry.sdk.metrics.export.MetricExporter
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import java.util.concurrent.TimeUnit

private val logger =
    System
        /*
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * !! IMPORTANT:                                                                       !!
         * !!     This statement has to always be executed first before **any** other code,    !!
         * !!     or else the property "java.util.logging.manager" may have no effect anymore! !!
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        .setProperty("java.util.logging.manager", org.apache.logging.log4j.jul.LogManager::class.java.name)
        .let { logger { } }

/*
How to test locally:

docker run --rm \
-e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \
-p 16686:16686 \
-p 4317:4317 \
-p 4318:4318 \
-p 9411:9411 \
jaegertracing/all-in-one:latest

UI available at http://localhost:16686/
*/
internal fun buildOpenTelemetryConfig(
    serviceName: String,
    endpointConfig: String = "http://traces_collector:4317",
): OpenTelemetry {
    val spanExporter =
        OtlpGrpcSpanExporter
            .builder()
            .setEndpoint(endpointConfig)
            .setTimeout(30, TimeUnit.SECONDS)
            .build()
    logger.debug { "Span exporter configured with endpoint: $endpointConfig" }

    val metricExporter = LoggingMetricExporter(OtlpGrpcMetricExporter.builder().setEndpoint(endpointConfig).build())
    logger.debug { "Metric exporter configured with endpoint: $endpointConfig" }
    val recordExporter = OtlpGrpcLogRecordExporter.builder().setEndpoint(endpointConfig).build()
    logger.debug { "Log record exporter configured with endpoint: $endpointConfig" }

    val resource =
        Resource
            .getDefault()
            .toBuilder()
            .put(AttributeKey.stringKey("service.name"), serviceName)
            .build()
    logger.debug { "Resource initialized with service name: $serviceName" }


    val tracerProvider =
        SdkTracerProvider
            .builder()
            .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
            .setResource(resource)
            .build()
    logger.debug { "Tracer provider configured" }

    val meterProvider =
        SdkMeterProvider
            .builder()
            .registerMetricReader(
                PeriodicMetricReader.builder(metricExporter).build(),
            ).setResource(resource)
            .build()
    logger.debug { "Meter provider configured" }

    val loggerProvider =
        SdkLoggerProvider
            .builder()
            .addLogRecordProcessor(
                BatchLogRecordProcessor.builder(recordExporter).build(),
            ).setResource(resource)
            .build()
    logger.debug { "Logger provider configured" }


    val openTelemetry =
        OpenTelemetrySdk
            .builder()
            .setTracerProvider(tracerProvider)
            .setMeterProvider(meterProvider)
            .setLoggerProvider(loggerProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .buildAndRegisterGlobal()
    logger.debug { "OpenTelemetry SDK built and registered globally" }

    LoggingTracer(openTelemetry.tracerProvider[serviceName])
    LoggingMeter(openTelemetry.meterProvider[serviceName])
    logger.debug { "LoggingTracer and LoggingMeter are now active" }


    logger.debug { "Periodic metric reader initialized with interval: 10 seconds" }



    Runtime.getRuntime().addShutdownHook(Thread {
        logger.debug { "Closing OpenTelemetry" }
        openTelemetry.close()
    })
    logger.debug { "Shutdown hook registered for OpenTelemetry components" }

    return openTelemetry
}


class LoggingTracer(private val delegate: Tracer) : Tracer by delegate {
    override fun spanBuilder(spanName: String): SpanBuilder {
        logger.debug { "Creating span builder for span: $spanName" }
        return delegate.spanBuilder(spanName)
    }

}

class LoggingMeter(private val delegate: Meter) : Meter by delegate {
    override fun counterBuilder(name: String): LongCounterBuilder {
        logger.debug { "Creating counter metric: $name" }
        return delegate.counterBuilder(name)
    }

    override fun histogramBuilder(name: String): DoubleHistogramBuilder {
        logger.debug { "Creating histogram metric: $name" }
        return delegate.histogramBuilder(name)
    }
}

class LoggingMetricExporter(private val delegate: OtlpGrpcMetricExporter) : MetricExporter by delegate {
    override fun export(metrics: Collection<MetricData>): CompletableResultCode {
        val result = delegate.export(metrics)
        if (result.isSuccess) {
            logger.debug { "Metrics exported successfully" }
        } else {
            logger.error { "Metric export failed ${result.failureThrowable}" }
        }
        return result
    }
}
