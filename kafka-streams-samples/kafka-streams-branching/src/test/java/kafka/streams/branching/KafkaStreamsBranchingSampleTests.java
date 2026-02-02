/*
 * Copyright 2018 the original author or authors.
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

package kafka.streams.branching;

import java.time.Duration;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka(topics = {"words", "english-counts", "french-counts", "spanish-counts"}, count = 1,
		bootstrapServersProperty = "spring.cloud.stream.kafka.streams.binder.brokers")
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaStreamsBranchingSampleTests {

	@Autowired
	EmbeddedKafkaBroker embeddedKafka;

	private Consumer<String, String> consumer;

	@Autowired
	StreamsBuilderFactoryBean streamsBuilderFactoryBean;

	@BeforeEach
	public void before() {
		streamsBuilderFactoryBean.setCloseTimeout(0);
	}

	@BeforeAll
	void setUp() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group", "false", embeddedKafka);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
		consumer = cf.createConsumer();
		embeddedKafka.consumeFromEmbeddedTopics(consumer, "english-counts", "french-counts", "spanish-counts");
	}

	@AfterAll
	void tearDown() {
		consumer.close();
	}

	@Test
	public void testKafkaStreamsWordCountProcessor() throws InterruptedException {
		Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
		DefaultKafkaProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
		try {
			KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf, true);
			template.setDefaultTopic("words");
			template.sendDefault("english");
			template.sendDefault("french");
			template.sendDefault("spanish");
			Thread.sleep(2000);
			ConsumerRecord<String, String> cr = KafkaTestUtils.getSingleRecord(consumer, "english-counts", Duration.ofSeconds(5));
			assertThat(cr.value().contains("english")).isTrue();
			cr = KafkaTestUtils.getSingleRecord(consumer, "french-counts", Duration.ofSeconds(5));
			assertThat(cr.value().contains("french")).isTrue();
			cr = KafkaTestUtils.getSingleRecord(consumer, "spanish-counts", Duration.ofSeconds(5));
			assertThat(cr.value().contains("spanish")).isTrue();
		}
		finally {
			pf.destroy();
		}
	}

}
