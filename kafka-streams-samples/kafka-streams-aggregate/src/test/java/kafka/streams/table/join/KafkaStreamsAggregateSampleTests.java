package kafka.streams.table.join;

import java.time.Duration;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@EmbeddedKafka(topics = "foobar", count = 1,
		bootstrapServersProperty = "spring.cloud.stream.kafka.streams.binder.brokers")
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KafkaStreamsAggregateSampleTests {

	@Autowired
	EmbeddedKafkaBroker embeddedKafka;

	@Autowired
	StreamsBuilderFactoryBean streamsBuilderFactoryBean;

	@LocalServerPort
	int randomServerPort;

	@BeforeEach
	public void before() {
		streamsBuilderFactoryBean.setCloseTimeout(0);
	}

	@Test
	public void testKafkaStreamsWordCountProcessor() throws Exception {
		Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
		Serde<DomainEvent> domainEventSerde = new JsonSerde<>(DomainEvent.class);

		senderProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		senderProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, domainEventSerde.serializer().getClass());

		DefaultKafkaProducerFactory<String, DomainEvent> pf = new DefaultKafkaProducerFactory<>(senderProps);
		try {

			KafkaTemplate<String, DomainEvent> template = new KafkaTemplate<>(pf, true);
			template.setDefaultTopic("foobar");

			DomainEvent ddEvent = new DomainEvent();
			ddEvent.setBoardUuid("12345");
			ddEvent.setEventType("create-domain-event");

			template.sendDefault("", ddEvent);

			// Wait for Kafka Streams to reach RUNNING state before querying the state store
			await().atMost(Duration.ofSeconds(30)).pollInterval(Duration.ofMillis(500)).until(() -> {
				KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
				return kafkaStreams != null && kafkaStreams.state() == KafkaStreams.State.RUNNING;
			});

			RestTemplate restTemplate = new RestTemplate();
			String fooResourceUrl
					= "http://localhost:" + randomServerPort + "/events";

			// Poll for the result since the state store may need time to process the record
			await().atMost(Duration.ofSeconds(30)).pollInterval(Duration.ofMillis(500)).untilAsserted(() -> {
				ResponseEntity<String> response
						= restTemplate.getForEntity(fooResourceUrl, String.class);
				assertThat(response.getBody()).contains("create-domain-event");
			});
		}
		finally {
			pf.destroy();
		}
	}

}
