package io.honeycomb.otel.fibonacci;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FibonacciApplication {


	public static void main(String[] args) {

//		HoneycombSdk honeycomb = new HoneycombSdk.Builder()
//				.setApiKey(System.getenv("HONEYCOMB_API_KEY"))
//				.setDataset(System.getenv("HONEYCOMB_DATASET"))
//				.setSampler(new DeterministicTraceSampler(1))  // optional
//				.setServiceName("fibonacci-from-honeycomb-sdk")         // optional
//				.build();

		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(
						BatchSpanProcessor.builder(
								OtlpGrpcSpanExporter.builder()
										.build()).
								build())
				.addSpanProcessor(SimpleSpanProcessor.create(new LoggingSpanExporter()))
				.build();

		OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
				.setTracerProvider(sdkTracerProvider)
				.setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
				.buildAndRegisterGlobal();

		openTelemetry.getTracer("poo").spanBuilder("butts")
				.setAttribute("carrot", "lizard").startSpan().end();

		SpringApplication.run(FibonacciApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
