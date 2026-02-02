/*
 * Copyright 2017-2019 the original author or authors.
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

package org.springframework.cloud.stream.testing.source;


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
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;

/**
 * The Spring Boot-base test-case to demonstrate how can we test Spring Cloud Stream applications
 * with available testing tools.
 *
 * @author Artem Bilan
 *
 */
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = "spring.cloud.stream.poller.fixed-delay=1",
		classes = { OddEvenSource.class, TestChannelBinderConfiguration.class })
@ImportAutoConfiguration(exclude = {
		KafkaAutoConfiguration.class,
		KafkaMetricsAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		TransactionAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
@DirtiesContext
class OddEvenSourceTests {

	@Autowired
	private OutputDestination output;

	@Test
	void testMessages() {
		Message<byte[]> result = this.output.receive(5000);
		assertThat(result).isNotNull();
		assertThat(new String(result.getPayload())).isEqualTo("odd");

		result = this.output.receive(5000);
		assertThat(result).isNotNull();
		assertThat(new String(result.getPayload())).isEqualTo("even");

		result = this.output.receive(5000);
		assertThat(result).isNotNull();
		assertThat(new String(result.getPayload())).isEqualTo("odd");

		result = this.output.receive(5000);
		assertThat(result).isNotNull();
		assertThat(new String(result.getPayload())).isEqualTo("even");
	}

}
