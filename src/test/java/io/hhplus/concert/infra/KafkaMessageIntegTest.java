package io.hhplus.concert.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(topics = "test-topic",  brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class KafkaMessageIntegTest {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    TestMessageConsumer testMessageConsumer;

    @Test
    @DisplayName("Kafka 메세지 발행 및 Consume 테스트")
    void testKafkaMessage() {
        //given
        var cntBeforeTest = testMessageConsumer.getMessageConsumedCnt();

        //when
        kafkaTemplate.send("test-topic", "test-message");

        //then
        await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(testMessageConsumer.getMessageConsumedCnt()).isGreaterThanOrEqualTo(cntBeforeTest + 1);
        });
    }
}
