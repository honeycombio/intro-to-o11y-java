package io.honeycomb.otel.fibonacci;

import io.honeycomb.opentelemetry.HoneycombSdk;
import io.honeycomb.opentelemetry.sdk.trace.samplers.DeterministicTraceSampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FibonacciApplication {

	
	public static void main(String[] args) {

		HoneycombSdk honeycomb = new HoneycombSdk.Builder()
				.setApiKey(System.getenv("HONEYCOMB_API_KEY"))
				.setDataset(System.getenv("HONEYCOMB_DATASET"))
				.setSampler(new DeterministicTraceSampler(1))  // optional
				.setServiceName("fibonacci-from-honeycomb-sdk")         // optional
				.build();

		SpringApplication.run(FibonacciApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
