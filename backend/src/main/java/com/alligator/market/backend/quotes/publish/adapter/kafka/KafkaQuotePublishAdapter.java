package com.alligator.market.backend.quotes.publish.adapter.kafka;

import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.ports.QuotePublishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Адаптер публикации тиков котировок в Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaQuotePublishAdapter implements QuotePublishPort {

    private static final String TOPIC = "ticks.raw";

    private final KafkaTemplate<String, com.alligator.market.domain.avro.quotes.stream.QuoteTick> template;

    //==========================================
    // Опубликовать тик котировки в топике Kafka
    //==========================================
    @Override
    public void publish(QuoteTick tick) {
        var avroTick = com.alligator.market.domain.avro.quotes.stream.QuoteTick.newBuilder()
                .setPair(tick.pair())
                .setBid(tick.bid())
                .setAsk(tick.ask())
                .setTs(tick.ts())
                .setProvider(tick.provider())
                .build();
        template.send(TOPIC, tick.pair(), avroTick);
        log.debug("Tick {} sent to Kafka", avroTick);
    }
}
