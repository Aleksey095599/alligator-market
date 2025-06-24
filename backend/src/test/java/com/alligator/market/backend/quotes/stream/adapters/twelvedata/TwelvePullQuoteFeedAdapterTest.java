package com.alligator.market.backend.quotes.stream.adapters.twelvedata;

import com.alligator.market.backend.quotes.stream.providers.adapters.twelve_free_mid_pull.TwelvePullQuoteFeedAdapter;
import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.ports.QuotePublishPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Временный тест для ручного запуска получения котировки от TwelveData.
 */
@SpringBootTest
public class TwelvePullQuoteFeedAdapterTest {

    @Autowired
    private TwelvePullQuoteFeedAdapter adapter;

    @Autowired
    private QuotePublishPort publisher;

    @Test
    void fetchQuotePublish() {
        try {
            QuoteTick tick = adapter.fetchQuote("EURUSD");
            publisher.publish(tick);
            System.out.println("EURUSD price: " + tick.bid());
        } catch (Exception e) {
            System.out.println("Cannot fetch quote: " + e.getMessage());
        }
    }
}
