/*
 * Copyright 2015-2016 the original author or authors.
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

package multibinder;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.cloud.stream.binder.BinderFactory;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder;
import org.springframework.cloud.stream.binder.kafka.properties.KafkaBinderConfigurationProperties;
import org.springframework.cloud.stream.binder.kafka.properties.KafkaConsumerProperties;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
public class TwoKafkaBindersApplicationTest {

	private static final EmbeddedKafkaBroker kafkaTestSupport1;
	private static final EmbeddedKafkaBroker kafkaTestSupport2;

	static {
		kafkaTestSupport1 = new EmbeddedKafkaKraftBroker(1, 1, "input", "output");
		kafkaTestSupport1.afterPropertiesSet();
		kafkaTestSupport2 = new EmbeddedKafkaKraftBroker(1, 1, "input", "output");
		kafkaTestSupport2.afterPropertiesSet();
	}

	@DynamicPropertySource
	static void kafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("kafkaBroker1", kafkaTestSupport1::getBrokersAsString);
		registry.add("kafkaBroker2", kafkaTestSupport2::getBrokersAsString);
		registry.add("zk1", () -> "localhost");
		registry.add("zk2", () -> "localhost");
	}

	@AfterAll
	static void tearDown() {
		kafkaTestSupport1.destroy();
		kafkaTestSupport2.destroy();
	}

	@Autowired
	private BinderFactory binderFactory;

	@Test
	public void contextLoads() {
		Binder<MessageChannel, ?, ?> binder1 = binderFactory.getBinder("kafka1", MessageChannel.class);
		KafkaMessageChannelBinder kafka1 = (KafkaMessageChannelBinder) binder1;
		DirectFieldAccessor directFieldAccessor1 = new DirectFieldAccessor(kafka1);
		KafkaBinderConfigurationProperties configuration1 =
				(KafkaBinderConfigurationProperties) directFieldAccessor1.getPropertyValue("configurationProperties");
		assertThat(configuration1.getBrokers()).hasSize(1);
		assertThat(configuration1.getBrokers()[0]).isEqualTo(kafkaTestSupport1.getBrokersAsString());

		Binder<MessageChannel, ?, ?> binder2 = binderFactory.getBinder("kafka2", MessageChannel.class);
		KafkaMessageChannelBinder kafka2 = (KafkaMessageChannelBinder) binder2;
		DirectFieldAccessor directFieldAccessor2 = new DirectFieldAccessor(kafka2);
		KafkaBinderConfigurationProperties configuration2 =
				(KafkaBinderConfigurationProperties) directFieldAccessor2.getPropertyValue("configurationProperties");
		assertThat(configuration2.getBrokers()).hasSize(1);
		assertThat(configuration2.getBrokers()[0]).isEqualTo(kafkaTestSupport2.getBrokersAsString());
	}

	@Test
	public void messagingWorks() {
		QueueChannel dataConsumer = new QueueChannel();
		((KafkaMessageChannelBinder) binderFactory.getBinder("kafka2", MessageChannel.class)).bindConsumer("dataOut", UUID.randomUUID().toString(),
				dataConsumer, new ExtendedConsumerProperties<>(new KafkaConsumerProperties()));

		//receiving test message sent by the test producer in the application
		Message<?> receive = dataConsumer.receive(60_000);
		assertThat(receive).isNotNull();
		assertThat(receive.getPayload()).isIn("FOO".getBytes(), "BAR".getBytes());
	}

}
