package com.alligator.market.backend.quotes.stream.adapters.twelvedata;

import com.alligator.market.domain.quotes.stream.QuoteTick;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Временный тест для ручного запуска получения котировки от TwelveData.
 */
@SpringBootTest
public class TwelveDataQuoteFeedAdapterTest {

    @Autowired
    private TwelveDataQuoteFeedAdapter adapter;

    @Test
    void fetchQuotePrint() {
        try {
            QuoteTick tick = adapter.fetchQuote("EURUSD");
            System.out.println("EURUSD price: " + tick.bid());
        } catch (Exception e) {
            System.out.println("Cannot fetch quote: " + e.getMessage());
        }
    }
}
