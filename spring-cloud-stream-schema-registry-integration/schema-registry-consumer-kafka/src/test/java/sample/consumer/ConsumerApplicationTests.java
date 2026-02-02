/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.consumer;

import com.example.Sensor;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.cloud.stream.schema.registry.SchemaReference;
import org.springframework.cloud.stream.schema.registry.SchemaRegistrationResponse;
import org.springframework.cloud.stream.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MimeType;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@ExtendWith(OutputCaptureExtension.class)
public class ConsumerApplicationTests {

	private static final int AWAIT_DURATION = 10;

	@Test
	void test(CapturedOutput output) {
		try (ConfigurableApplicationContext context =
				 new SpringApplicationBuilder(
					 TestChannelBinderConfiguration
						 .getCompleteConfiguration(
							 ConsumerApplication.class,
							 StubSchemaRegistryClientConfig.class))
					 .web(WebApplicationType.NONE)
					 .run(
						"--spring.cloud.stream.schema.avro.ignore-schema-registry-server=true",
						"--spring.main.allow-bean-definition-overriding=true"
					 )
		) {

			StreamBridge streamBridge = context.getBean("streamBridgeUtils", StreamBridge.class);
			Random random = new Random();
			Sensor sensor = new Sensor();
			sensor.setId(UUID.randomUUID() + "-v1");
			sensor.setAcceleration(random.nextFloat() * 10);
			sensor.setVelocity(random.nextFloat() * 100);
			MimeType mimeType = new MimeType("application", "*+avro");
			streamBridge.send("process-in-0", sensor, mimeType);

			Awaitility.await().atMost(Duration.ofSeconds(AWAIT_DURATION))
				.until(() -> output.toString().contains("[INPUT-RECEIVED]: {\"id\":"));
		}
	}

	@Configuration(proxyBeanMethods = false)
	static class StubSchemaRegistryClientConfig {

		@Bean
		SchemaRegistryClient schemaRegistryClient() {
			return new SchemaRegistryClient() {
				@Override
				public SchemaRegistrationResponse register(String subject, String format, String schema) {
					SchemaRegistrationResponse response = new SchemaRegistrationResponse();
					response.setId(1);
					response.setSchemaReference(new SchemaReference(subject, 1, format));
					return response;
				}

				@Override
				public String fetch(SchemaReference schemaReference) {
					return "{}";
				}

				@Override
				public String fetch(int id) {
					return "{}";
				}
			};
		}
	}
}
