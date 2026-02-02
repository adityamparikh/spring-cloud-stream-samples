/*
 * Copyright 2017 the original author or authors.
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

package org.springframework.cloud.stream.testing.processor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.kafka.autoconfigure.metrics.KafkaMetricsAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.transaction.autoconfigure.TransactionAutoConfiguration;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;

/**
 * The Spring Boot-base test-case to demonstrate how can we test Spring Cloud Stream applications
 * with available testing tools.
 *
 * @author Artem Bilan
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
		classes = { ToUpperCaseProcessor.class, TestChannelBinderConfiguration.class })
@ImportAutoConfiguration(exclude = {
		KafkaAutoConfiguration.class,
		KafkaMetricsAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		TransactionAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
@DirtiesContext
class ToUpperCaseProcessorTests {

	@Autowired
	private InputDestination input;

	@Autowired
	private OutputDestination output;

	@Test
	void testMessages() {
		this.input.send(new GenericMessage<>("odd"));
		Message<byte[]> result = this.output.receive(5000);
		assertThat(result).isNotNull();
		assertThat(new String(result.getPayload())).isEqualTo("ODD");

		this.input.send(new GenericMessage<>("even"));
		result = this.output.receive(5000);
		assertThat(result).isNotNull();
		assertThat(new String(result.getPayload())).isEqualTo("EVEN");

		this.input.send(new GenericMessage<>("odd meets even"));
		result = this.output.receive(5000);
		assertThat(result).isNotNull();
		assertThat(new String(result.getPayload())).isEqualTo("ODD MEETS EVEN");

		this.input.send(new GenericMessage<>("nothing but the best test"));
		result = this.output.receive(5000);
		assertThat(result).isNotNull();
		assertThat(new String(result.getPayload())).isNotEqualTo("nothing but the best test");

		this.input.send(new GenericMessage<>("headers"));
		result = this.output.receive(5000);
		assertThat(result).isNotNull();
		assertThat(new String(result.getPayload())).isEqualTo("HEADERS");
	}

}
