package com.alligator.market.backend.quote.publish.adapter.kafka;

import com.alligator.market.domain.avro.QuoteTickAvro;
import com.alligator.market.domain.quote.QuoteTick;
import com.alligator.market.domain.quote.ports.QuotePublishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Адаптер публикации котировок в Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaQuotePublishAdapter implements QuotePublishPort {

    private static final String TOPIC = "ticks.raw";

    private final KafkaTemplate<String, QuoteTickAvro> template;

    //==========================================
    // Опубликовать тик котировки в топике Kafka
    //==========================================
    @Override
    public void publish(QuoteTick tick) {
        var avroTick = QuoteTickAvro.newBuilder()
                .setSymbol(tick.symbol())
                .setBid(tick.bid())
                .setAsk(tick.ask())
                .setTs(tick.ts())
                .setProvider(tick.provider())
                .build();
        template.send(TOPIC, tick.symbol(), avroTick);
        log.debug("Tick {} sent to Kafka", avroTick);
    }
}
