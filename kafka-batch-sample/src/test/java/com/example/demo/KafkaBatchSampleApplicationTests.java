package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(topics = {"batch-in", "batch-out"}, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class KafkaBatchSampleApplicationTests {

	@Test
	void contextLoads() {
	}

}
