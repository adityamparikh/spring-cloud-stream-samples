/*
 * Copyright 2020 the original author or authors.
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

package com.example.kafkanativeserialization;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka(topics = "topic2", count = 1,
		bootstrapServersProperty = "spring.cloud.stream.kafka.binder.brokers")
@SpringBootTest
public class KafkaNativeSerializationApplicationTests {

	private static final String INPUT_TOPIC = "topic1";
	private static final String OUTPUT_TOPIC = "topic2";
	private static final String GROUP_NAME = "nativeSerializationTest";

	@Autowired
	EmbeddedKafkaBroker embeddedKafka;

	@Test
	public void testSendReceive() {
		Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
		senderProps.put("value.serializer", StringSerializer.class);
		DefaultKafkaProducerFactory<byte[], String> pf = new DefaultKafkaProducerFactory<>(senderProps);
		KafkaTemplate<byte[], String> template = new KafkaTemplate<>(pf, true);
		template.setDefaultTopic(INPUT_TOPIC);
		template.sendDefault("foo");

		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(GROUP_NAME, "false", embeddedKafka);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerProps.put("value.deserializer", MyJsonDeserializer.class);
		DefaultKafkaConsumerFactory<byte[], Person> cf = new DefaultKafkaConsumerFactory<>(consumerProps);

		Consumer<byte[], Person> consumer = cf.createConsumer();
		consumer.subscribe(Collections.singleton(OUTPUT_TOPIC));
		ConsumerRecords<byte[], Person> records = consumer.poll(Duration.ofSeconds(10));
		consumer.commitSync();

		assertThat(records.count()).isEqualTo(1);
		assertThat(new String(records.iterator().next().value().getName())).isEqualTo("foo");
	}
}
