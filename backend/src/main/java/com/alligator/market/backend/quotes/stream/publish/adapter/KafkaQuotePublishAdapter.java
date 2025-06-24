package com.alligator.market.backend.quotes.stream.publish.adapter;

import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.ports.QuotePublishPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaQuotePublishAdapter implements QuotePublishPort {
    private static final String TOPIC = "ticks.raw";
    private final KafkaTemplate<String, QuoteTick> kafka;   // value‑serializer = Avro

    @Override
    public void publish(QuoteTick tick) {
        kafka.send(TOPIC, tick.pair(), tick);
    }

}
