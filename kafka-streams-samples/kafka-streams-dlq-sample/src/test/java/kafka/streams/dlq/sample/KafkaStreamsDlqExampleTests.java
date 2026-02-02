package kafka.streams.dlq.sample;

import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka(topics = {"words", "words-count-dlq"}, count = 1,
		bootstrapServersProperty = "spring.cloud.stream.kafka.streams.binder.brokers")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaStreamsDlqExampleTests {

	@Autowired
	EmbeddedKafkaBroker embeddedKafka;

	private Consumer<String, String> consumer;

	@BeforeAll
	void setUp() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group", "false", embeddedKafka);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
		consumer = cf.createConsumer();
		embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "words-count-dlq");
	}

	@AfterAll
	void tearDown() {
		consumer.close();
	}

	@Test
	public void testKafkaStreamsWordCountProcessor() {
		Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
		DefaultKafkaProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
		try {
			KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf, true);
			template.setDefaultTopic("words");
			template.sendDefault("foobar");
			ConsumerRecords<String, String> cr = KafkaTestUtils.getRecords(consumer);
			assertThat(cr.count()).isGreaterThanOrEqualTo(1);
		}
		finally {
			pf.destroy();
		}
	}

}
