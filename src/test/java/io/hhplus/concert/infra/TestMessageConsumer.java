package io.hhplus.concert.infra;

import groovy.util.logging.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class TestMessageConsumer {
    private static final Logger log = LoggerFactory.getLogger(TestMessageConsumer.class);
    AtomicInteger messageConsumedCnt = new AtomicInteger(0);
    @KafkaListener(topics = "test-topic")
    public void listen(ConsumerRecord<String, String> record) {
        messageConsumedCnt.incrementAndGet();
    }

    public int getMessageConsumedCnt() {
        return messageConsumedCnt.get();
    }
}
